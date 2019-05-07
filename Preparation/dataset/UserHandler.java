/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.PartialResultException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;

import net.sf.hibernate.Session;

import org.exoplatform.commons.utils.ObjectPageList;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.impl.UserImpl;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.Node;

import sun.misc.BASE64Encoder;

import com.sun.jndi.ldap.ctl.PagedResultsControl;
import com.sun.jndi.ldap.ctl.PagedResultsResponseControl;
import com.sun.jndi.ldap.ctl.SortControl;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class UserHandler extends BaseHandler {
	protected List listeners_;
	protected LDAPService ldapService;
	protected HibernateService hibernateService;

	public UserHandler(LDAPService ldapService,
			HibernateService hibernateService) {
		this.ldapService = ldapService;
		super.ldapService = ldapService;
		this.hibernateService = hibernateService;
		listeners_ = new ArrayList(5);
	}

	public void addUserEventListener(UserEventListener listener) {
		listeners_.add(listener);
	}

	final public List getUserEventListeners() {
	    return listeners_ ;
	}
	
	public boolean authenticate(String userName, String password)
			throws Exception {
		LdapContext ctx = null;
		boolean authenticated = false;
		try {
			ctx = ldapService.getLdapContext();
			String userDN = this.getDNFromUsername(userName);
			authenticated = ldapService.authenticate(userDN, password);
			if (authenticated){
				User user = this.findUserByDN(userDN, ctx);
				this.checkProfile(user.getUserName());
			}
		} catch (Exception e){
			// do nothing failed to authenticate
		} finally {
			ctx.close();
		}
		return authenticated;
	}

	public void checkProfile(String userName) throws Exception {
			User user = findUserByName(userName);
			checkProfile(user);
	}
	
	public void checkProfile(User user) throws Exception {
		PortalContainer manager = PortalContainer.getInstance();
		PortalConfigService portalConfigService = (PortalConfigService) manager
				.getComponentInstanceOfType(PortalConfigService.class);
		Node node = portalConfigService.getNodeNavigation(user.getUserName());
		if (node == null) {
			Session session = hibernateService.openSession();
			try {
				
				this.preSave(user, true, session);
				this.postSave(user, true, session);
				session.flush();
			} finally {
				hibernateService.closeSession(session);
			}
		}
	}
	
	public void createUser(User user) throws Exception {
		LdapContext ctx = null;
		Session session = null;
		try {
			ctx = ldapService.getLdapContext();
			session = hibernateService.openSession();
			
			User user1 = this.findUserByName(user.getUserName());
			if (user1 != null){
				this.checkProfile(user1);
			} else {
				preSave(user, true, session);
				createUserEntry(user);
				postSave(user, true, session);
				session.flush();
			}
		} finally {
			hibernateService.closeSession(session);
			ctx.close();
		}
	}

	protected void createUserEntry(User user) throws Exception {
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			String userDN = "cn="+user.getUserName()+","+(String)OrganizationServiceImpl.properties.get("ldap.users.url");
			if (userDN != null){
				NameParser parser = ctx.getNameParser("");
				Name name = parser.parse(userDN);
				BasicAttributes attrs = new BasicAttributes();
				
				// create objectclasses
				BasicAttribute oc = new BasicAttribute("objectClass");
				oc.add("top");
				oc.add("person");
				oc.add("organizationalPerson");
				oc.add("inetOrgPerson");
				attrs.put(oc);
				
				// create cn
				BasicAttribute cn = new BasicAttribute("cn", user.getUserName());
				attrs.put(cn);
				
				// create displayName
				BasicAttribute displayName = new BasicAttribute("displayName", user.getFullName());
				attrs.put(displayName);
				
				// create account
				BasicAttribute account = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute"), user.getUserName());
				attrs.put(account);
				
				// create lastname
				BasicAttribute lastName = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute"), user.getLastName());
				attrs.put(lastName);
				
				// create firstname
				BasicAttribute firstName = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute"), user.getFirstName());
				attrs.put(firstName);
				
				// create mail
				BasicAttribute mail = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute"), user.getEmail());
				attrs.put(mail);
				
				// create description
				BasicAttribute description  = new BasicAttribute("description", "Account for "+user.getFullName());
				attrs.put(description);
				
				// create password
				BasicAttribute password = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.password.attribute"), encryptPassword(user.getPassword()));
				attrs.put(password);
				
				// Create user in directory
				ctx.createSubcontext(name, attrs);
			}
		} finally {
			ctx.close();
		}
	}

	public User findUserByName(String userName) throws Exception {
		LdapContext ctx = null;
		User user = null;
		try {
			ctx = ldapService.getLdapContext();
			String userDN = this.getDNFromUsername(userName);
			user = this.findUserByDN(userDN, ctx);
		} finally {
			ctx.close();
		}
		return user;
	}

	protected User findUserByDN (String userDN, LdapContext ctx) throws Exception {
		User user = null;
		if (userDN != null){
			try {
				NameParser parser = ctx.getNameParser("");
				Name dn = parser.parse(userDN);
				Attributes attrs = ctx.getAttributes(dn);
				if (attrs != null){
					user = new UserImpl();
					user.setUserName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute")));
					user.setPassword("PASSWORD");
					user.setFirstName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute")));
					user.setLastName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute")));
					user.setEmail(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute")));
					user.setLastLoginTime(new Date());
					user.setCreatedDate(new Date());
					user.setFullName(user.getFirstName()+" "+user.getLastName());
				}
			} catch (NameNotFoundException e){
				
			}
		}
		return user;
	}
	
	
	public PageList findUsers(Query q) throws Exception {
		LdapContext ctx = null;
		List users = new ArrayList();
		String pageSizeString = (String)OrganizationServiceImpl.properties.get("ldap.page.size");
		String pageSleepString = (String)OrganizationServiceImpl.properties.get("ldap.page.sleep");
		int pageSize = 100;
		long pageSleep = 0;
		if (pageSizeString != null){
			pageSize = Integer.valueOf(pageSizeString).intValue();
		}
		if (pageSleepString != null){
			pageSleep = Long.valueOf(pageSleepString).longValue();
		}
		
		int itemCounter = pageSize;
		NamingEnumeration enumer;
		try {
			ctx = ldapService.getLdapContext();
			String filter = null;
			ArrayList list = new ArrayList();
			if (q.getUserName() != null && q.getUserName().length() > 0) {
				list.add("(" + (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute") + "=" + q.getUserName() + ")");
			}
			if (q.getFirstName() != null && q.getFirstName().length() > 0) {
				list.add("(" + (String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute") + "=" + q.getFirstName() + ")");
			}
			if (q.getLastName() != null && q.getLastName().length() > 0) {
				list.add("(" + (String)OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute") + "=" + q.getLastName() + ")");
			}
			if (q.getEmail() != null && q.getEmail().length() > 0) {
				list.add("(" + (String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute") + "=" + q.getEmail() + ")");
			}
			
			if (list.size() > 0){
				StringBuffer buffer = new StringBuffer();
				buffer.append("(&");
				if (list.size() > 1){
					for (int x = 0; x < list.size(); x++){
						if (x == (list.size() - 1)){
							buffer.append(list.get(x));
						} else {
							buffer.append(list.get(x)+" || ");
						}
					}
				} else {
					buffer.append(list.get(0));
				}
				buffer.append(" (" +(String)OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter") + ") )");
				filter = buffer.toString();
			} else {
				filter = (String)OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter");
			}
			String searchBase = (String)OrganizationServiceImpl.properties.get("ldap.base.url");
			
			Control control[] = new Control[2];
			String keys[] = { (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute") };
			SortControl sortControl = new SortControl(keys, false);
			control[0] = sortControl;
			PagedResultsControl pagedResultsControl = new PagedResultsControl(pageSize);
			control[1] = pagedResultsControl;
			
			
			ctx.setRequestControls(control);
			while (itemCounter >= pageSize) {
				itemCounter = 0;
				SearchControls constraints = new SearchControls();
				constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

				enumer = ctx.search(searchBase, filter, constraints);
				while (enumer.hasMore()) {
					itemCounter += 1;
					SearchResult result = (SearchResult) enumer.next();
					Attributes attrs = result.getAttributes();
					// Create user object and add to arraylist
					User user = new UserImpl();
					user.setUserName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute")));
					user.setFirstName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute")));
					user.setLastName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.lasttname.attribute")));
					user.setEmail(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute")));
					user.setCreatedDate(new Date());
					user.setLastLoginTime(new Date());
					user.setPassword("PASSWORD");
					users.add(user);
				}
				if (itemCounter >= pageSize) {
					Control[] responseControls = ctx.getResponseControls();
					for (int i = 0; i < responseControls.length; i++) {
						if (responseControls[i] instanceof PagedResultsResponseControl) {
							pagedResultsControl = new PagedResultsControl(
									pageSize,
									((PagedResultsResponseControl) responseControls[i])
											.getCookie(), true);
							ctx.setRequestControls(new Control[] { pagedResultsControl });
						}
					}
				}
				if (pageSleep > 0){
					Thread.sleep(pageSleep);
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}

		return new ObjectPageList(users, 20);
	}

	public PageList findUsersByGroup(String groupId) throws Exception {
		LdapContext ctx = null;
		ArrayList users = new ArrayList();
		TreeMap map = new TreeMap();
		
		try {
			ctx = ldapService.getLdapContext();
			String groupDN = this.getGroupDNFromGroupId(groupId);
			String searchBase = groupDN;
			String filter = (String)OrganizationServiceImpl.properties.get("ldap.role.objectclass.filter");
			
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			NamingEnumeration results = ctx.search(searchBase,
					filter,
					constraints);
			if(results.hasMore()){
				SearchResult sr = (SearchResult) results.next();
				Attributes attrs = sr.getAttributes();
				List members = this.getAttributes(attrs, (String)OrganizationServiceImpl.properties.get("ldap.group.member.attribute"));
				for (int x = 0; x < members.size(); x++){
					User user = this.findUserByDN((String)members.get(x), ctx);
					map.put(user.getUserName(), user);
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		for (Iterator i = map.keySet().iterator(); i.hasNext();) {
			User user = (User) map.get(i.next());
			users.add(user);
		}
		return new ObjectPageList(users, 20);
	}

	public Collection findUsersByGroupAndRole(String groupName, String role)
			throws Exception {
		LdapContext ctx = null;
		ArrayList users = new ArrayList();
		TreeMap map = new TreeMap();
		try {
			ctx = ldapService.getLdapContext();
			String groupDN = this.getGroupDNFromGroupId(groupName);
			String scopedRoleDN = (String)OrganizationServiceImpl.properties.get("ldap.group.member.attribute") + "=" + role + "," + groupDN;
			try {
				Attributes attrs = ctx.getAttributes(scopedRoleDN);
				if (attrs != null){
					List members = this.getAttributes(attrs, (String)OrganizationServiceImpl.properties.get("ldap.group.member.attribute"));
					for (int x = 0; x < members.size(); x++){
						User user = this.findUserByDN((String)members.get(x), ctx);
						users.add(user);
					}
				}
			} catch (NameNotFoundException e){
				
			}
		} finally {
			ctx.close();
		}
		return users;
	}

	public PageList getUserPageList(int resultPageSize) throws Exception {
		LdapContext ctx = null;
		List users = new ArrayList();
		NamingEnumeration enumer;
		String pageSizeString = (String)OrganizationServiceImpl.properties.get("ldap.page.size");
		String pageSleepString = (String)OrganizationServiceImpl.properties.get("ldap.page.sleep");
		int pageSize = 100;
		long pageSleep = 0;
		if (pageSizeString != null){
			pageSize = Integer.valueOf(pageSizeString).intValue();
		}
		if (pageSleepString != null){
			pageSleep = Long.valueOf(pageSleepString).longValue();
		}
		int itemCounter = pageSize;
		
		try {
			ctx = ldapService.getLdapContext();
			
			String searchBase = (String)OrganizationServiceImpl.properties.get("ldap.base.url");
			String filter = (String)OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter");
			
			Control control[] = new Control[2];
			PagedResultsControl pagedResultsControl = new PagedResultsControl(pageSize);
			control[1] = pagedResultsControl;
			String keys[] = {(String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute") };
			SortControl sortControl = new SortControl(keys, false);
			control[0] = sortControl;
			
			ctx.setRequestControls(control);
			while (itemCounter >= pageSize) {
				itemCounter = 0;
				SearchControls constraints = new SearchControls();
				constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

				enumer = ctx.search(searchBase, filter, constraints);
				while (enumer.hasMore()) {
					itemCounter += 1;
					SearchResult result = (SearchResult) enumer.next();
					Attributes attrs = result.getAttributes();
					// Create user object and add to arraylist
					User user = new UserImpl();
					user.setUserName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute")));
					user.setFirstName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute")));
					user.setLastName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.lasttname.attribute")));
					user.setEmail(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute")));
					user.setCreatedDate(new Date());
					user.setLastLoginTime(new Date());
					user.setPassword("PASSWORD");
					users.add(user);
				}
				if (itemCounter >= pageSize) {
					Control[] responseControls = ctx.getResponseControls();
					for (int i = 0; i < responseControls.length; i++) {
						if (responseControls[i] instanceof PagedResultsResponseControl) {
							pagedResultsControl = new PagedResultsControl(
									pageSize,
									((PagedResultsResponseControl) responseControls[i])
											.getCookie(), true);
							ctx.setRequestControls(new Control[] { pagedResultsControl });
						}
					}
				}
				if (pageSleep > 0){
					Thread.sleep(pageSleep);
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return new ObjectPageList(users, resultPageSize);
	}

	protected void postDelete(User user, Session session) throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			UserEventListener listener = (UserEventListener) listeners_.get(i);
			listener.postDelete(user, xresources);
		}
	}

	protected void postSave(User user, boolean isNew, Session session)
			throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			UserEventListener listener = (UserEventListener) listeners_.get(i);
			listener.postSave(user, isNew, xresources);
		}
	}

	protected void preDelete(User user, Session session) throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			UserEventListener listener = (UserEventListener) listeners_.get(i);
			listener.preDelete(user, xresources);
		}
	}

	protected void preSave(User user, boolean isNew, Session session)
			throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			UserEventListener listener = (UserEventListener) listeners_.get(i);
			listener.preSave(user, isNew, xresources);
		}
	}

	public User removeUser(String userName) throws Exception {
		LdapContext ctx = null;
		Session session = null;
		User user = null;
		try {
			ctx = ldapService.getLdapContext();
			session = hibernateService.openSession();
			String userDN = this.getDNFromUsername(userName);
			user = this.findUserByDN(userDN, ctx);
			if (user != null){
				preDelete(user, session);
				ctx.destroySubcontext(userDN);
				postDelete(user, session);
				UserProfileHandler.removeUserProfileEntry(user.getUserName(), session);
			}
		} finally {
			hibernateService.closeSession(session);
			ctx.close();
		}
		return user;
	}

	/* This method use with importer/exporter */
	protected void removeUserEntry(User user) throws Exception {
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			String userDN = this.getDNFromUsername(user.getUserName());
			try {
				NameParser parser = ctx.getNameParser("");
				Name dn = parser.parse(userDN);
				Attributes attrs = ctx.getAttributes(dn);
				if (attrs != null){
					ctx.destroySubcontext(userDN);
				}
			} catch (NameNotFoundException e){
				
			}
		} finally {
			ctx.close();
		}
	}

	public void saveUser(User user) throws Exception {
		LdapContext ctx = null;
		Session session = null;
		try {
			ctx = ldapService.getLdapContext();
			session = hibernateService.openSession();
			String userDN = this.getDNFromUsername(user.getUserName());
			if (userDN != null){
				User existingUser = this.findUserByDN(userDN, ctx);
				
				NameParser parser = ctx.getNameParser("");
				Name name = parser.parse(userDN);

				ArrayList modifications = new ArrayList();
				
				// update displayName & description
				if (!user.getFullName().equals(existingUser.getFullName())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("displayName", user.getFullName()));
					modifications.add(mod);
					mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("description", "Account for "+user.getFullName()));
					modifications.add(mod);
				}
				
				// update account name
				if (!user.getUserName().equals(existingUser.getUserName())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute"), user.getUserName()));
					modifications.add(mod);
				}
				
				// update last name
				if (!user.getLastName().equals(existingUser.getLastName())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute"), user.getLastName()));
					modifications.add(mod);
				}
				
				// update first name
				if (!user.getFirstName().equals(existingUser.getFirstName())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute"), user.getFirstName()));
					modifications.add(mod);
				}
				
				// update email
				if (!user.getEmail().equals(existingUser.getEmail())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute"), user.getEmail()));
					modifications.add(mod);
				}
				
				if (!user.getPassword().equals("PASSWORD")){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.password.attribute"), encryptPassword(user.getPassword())));
					modifications.add(mod);
				}
				
				ModificationItem[] mods = new ModificationItem[modifications.size()];
				modifications.toArray(mods);
				preSave(user, false, session);
				ctx.modifyAttributes(name, mods);
				postSave(user, false, session);
			}
		} finally {
			session.flush();
			hibernateService.closeSession(session);
			ctx.close();
		}
	}
	
	
	private byte[] encryptPassword(String password) throws Exception {
		SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
		MessageDigest md = MessageDigest.getInstance("SHA");
		String PWDALG = "{SSHA}";
		
		BASE64Encoder b64enc = new BASE64Encoder();
		ByteArrayInputStream bis;
		ByteArrayOutputStream bos;
		byte[] salt = new byte[4];
		byte[] digest;
		byte[] buf;
		rand.nextBytes(salt);
		md.reset();
		md.update(password.getBytes());
		md.update(salt);
		digest = md.digest();
		buf = new byte[digest.length + salt.length];
		System.arraycopy(digest, 0, buf, 0, digest.length);
		System.arraycopy(salt, 0, buf, digest.length, salt.length);
		bis = new ByteArrayInputStream(buf);
		bos = new ByteArrayOutputStream();
		try {
			b64enc.encode(bis, bos);
			bis.close();
			bos.close();
		} catch (IOException e) {
		}
		buf = bos.toByteArray();
		digest = new byte[PWDALG.length() + buf.length];
		System.arraycopy(PWDALG.getBytes(), 0, digest, 0, PWDALG.length());
		System.arraycopy(buf, 0, digest, PWDALG.length(), buf.length);

		return digest;
	}
}