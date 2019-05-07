void method0() { 
/*************************************************************************
 * NOTE: below constants can be changed for each build,
 *       these constant MUST NOT break the compatibility
 *************************************************************************/
public static final String IMAGE_DIR = "/mvnplugin/mvnforum/images";
public static final String EMOTION_DIR = "/mvnplugin/mvnforum/images/emotion/";
public static final String CSS_FULLPATH = "/mvnplugin/mvnforum/css/style.css";
public static final String CSS_PREVIEW_FULLPATH = "/mvnplugin/mvnforum/css/style_preview.css";
public static final String CSS_BACKUP_FULLPATH = "/mvnplugin/mvnforum/css/style_backup.css";
public static final String LOGO_FULLPATH = "/mvnplugin/mvnforum/images/logo.gif";
// Note that we cannot put / at the end because getRealPath will remove it in Tomcat 4.1.7 :((  
public static final String UPLOADED_AVATAR_DIR = "/mvnplugin/mvnforum/upload/memberavatars";
public static final String RESOURCE_BUNDLE_NAME = "mvnForum_i18n";
/** value to control the flood prevention. Note value from 0 to 999 is belong to mvnCore */
public static final Integer FLOOD_ID_NEW_POST_PER_IP = new Integer(1000);
public static final Integer FLOOD_ID_NEW_MEMBER_PER_IP = new Integer(1001);
public static final Integer FLOOD_ID_LOGIN_PER_IP = new Integer(1002);
public static final Integer FLOOD_ID_NEW_MESSAGE_PER_IP = new Integer(1003);
public static final Integer FLOOD_ID_NEW_POST_PER_MEMBER = new Integer(1004);
public static final Integer FLOOD_ID_HTTP_REQUEST_PER_IP = new Integer(1005);
/** The maximum length of the email in database */
public static final int MAX_MEMBER_EMAIL_LENGTH = 60;
/** The maximum length of the member login name in database */
public static final int MAX_MEMBER_LOGIN_LENGTH = 30;
/** The type of search index: Disk */
public static final int SEARCH_INDEX_TYPE_DISK = 0;
/** The type of search index: Database */
public static final int SEARCH_INDEX_TYPE_DATABASE = 1;
public static final int MIN_MINUTES_TO_RATE_ALBUM_ITEM_AGAIN = 5;
public static final int MIN_MINUTES_TO_VOTE_POLL_AGAIN = 5;
public static final String TEMPLATE_SENDACTIVATECODE_PREFIX = "sendactivemailtemplate";
public static final String TEMPLATE_SENDACTIVATECODE_SUBJECT = "sendactivemailtemplate_subject.ftl";
public static final String TEMPLATE_SENDACTIVATECODE_BODY = "sendactivemailtemplate_body.ftl";
public static final String TEMPLATE_FORGOTPASSWORD_PREFIX = "forgotpasswordtemplate";
public static final String TEMPLATE_FORGOTPASSWORD_SUBJECT = "forgotpasswordtemplate_subject.ftl";
public static final String TEMPLATE_FORGOTPASSWORD_BODY = "forgotpasswordtemplate_body.ftl";
public static final String TEMPLATE_WATCHMAIL_DIGEST_PREFIX = "watchmailtemplate_digest";
public static final String TEMPLATE_WATCHMAIL_DIGEST_SUBJECT = "watchmailtemplate_digest_subject.ftl";
public static final String TEMPLATE_WATCHMAIL_DIGEST_BODY = "watchmailtemplate_digest_body.ftl";
public static final String TEMPLATE_WATCHMAIL_SINGLE_PREFIX = "watchmailtemplate_single";
public static final String TEMPLATE_WATCHMAIL_SINGLE_SUBJECT = "watchmailtemplate_single_subject.ftl";
public static final String TEMPLATE_WATCHMAIL_SINGLE_BODY = "watchmailtemplate_single_body.ftl";
public static final String TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX = "watchmailtemplate_gateway_digest";
public static final String TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_SUBJECT = "watchmailtemplate_gateway_digest_subject.ftl";
public static final String TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_BODY = "watchmailtemplate_gateway_digest_body.ftl";
public static final String TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_BODY_HTML = "watchmailtemplate_gateway_digest_body_html.ftl";
public static final String TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_PREFIX = "watchmailtemplate_gateway_single";
public static final String TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_SUBJECT = "watchmailtemplate_gateway_single_subject.ftl";
public static final String TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_BODY = "watchmailtemplate_gateway_single_body.ftl";
public static final String TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_PREFIX = "sendmailtemplate_postcensored";
public static final String TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_SUBJECT = "sendmailtemplate_postcensored_subject.ftl";
public static final String TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_BODY = "sendmailtemplate_postcensored_body.ftl";
}
