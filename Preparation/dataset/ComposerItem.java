// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.config;

import org.columba.core.config.DefaultItem;
import org.columba.core.xml.XmlElement;

/**
 * Composer window specific options.
 * 
 * @author fdietz
 *
 */

//
// composer_options.xml:
//
//<options>
//<gui>
//  <view id="0">
//    <window maximized="false" height="700" width="600" y="0" x="0" />
//    <toolbars infopanel="true" main="true" />
//    <splitpanes header="200" attachment="100" main="200" />
//    <addressbook enabled="false" />
//  </view>
//</gui>
//<spellcheck executable="/usr/bin/aspell" />
//<external_editor enabled="false" />
//<forward style="attachment" />
//<subject ask_if_empty="true" />
//<html send_as_multipart="true" enable="false" />
//</options>


public class ComposerItem extends DefaultItem {

	public final static String EXTERNAL_EDITOR = "external_editor";
	public final static String FORWARD = "forward";
	public final static String SUBJECT = "subject";
	public final static String HTML = "html";
	
	public final static String ENABLED_BOOL = "enabled";
	public final static String STYLE = "style";
	public final static String ASK_IF_EMPTY_BOOL = "ask_if_empty";
	public final static String SEND_AS_MULTIPART = "send_as_multipart";
	
	
	/**
	 * @param root
	 */
	public ComposerItem(XmlElement root) {
		super(root);
	}

}
