package com.absurd.circle.im.manager;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.ChatMessageType;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.data.util.JsonUtil;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

/**
 * Created by absurd on 14-4-25.
 */
public class XmppConnectionManager{
    public static final String SERVICE_NAME = "incircle";

    private XMPPConnection mConnection;
    private ConnectionConfiguration mConnectionConfig;
    private static XmppConnectionManager mXmppConnectionManager;


    public static XmppConnectionManager getInstance() {
        if (mXmppConnectionManager == null) {
            mXmppConnectionManager = new XmppConnectionManager();
        }
        return mXmppConnectionManager;
    }


    // init
    public XMPPConnection init() {
        if(mConnection == null) {
            Connection.DEBUG_ENABLED = false;
            ProviderManager pm = ProviderManager.getInstance();
            configure(pm);

            mConnectionConfig = new ConnectionConfiguration("168.63.140.70",5222, SERVICE_NAME);
            mConnectionConfig.setSASLAuthenticationEnabled(false);// 不使用SASL验证，设置为false
            mConnectionConfig.setCompressionEnabled(false);
            mConnectionConfig
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            // 允许自动连接
            mConnectionConfig.setReconnectionAllowed(false);
            // 允许登陆成功后更新在线状态
            mConnectionConfig.setSendPresence(false);
            // 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
            mConnection = new XMPPConnection(mConnectionConfig);
        }
        return mConnection;
    }

    /**
     *
     * 返回一个有效的xmpp连接,如果无效则返回空.
     *
     * @return
     * @author shimiso
     * @update 2012-7-4 下午6:54:31
     */
    public XMPPConnection getConnection() {
        if (mConnection == null) {
            //throw new RuntimeException("请先初始化XMPPConnection连接");
            return null;
        }
        return mConnection;
    }

    public boolean isInit(){
        return mConnection == null ? false : true;
    }


    /**
     *
     * 销毁xmpp连接.
     *
     * @author shimiso
     * @update 2012-7-4 下午6:55:03
     */
    public void disconnect() {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    public void login(String username, String pass){
        try {
            mConnection.connect();
            mConnection.login(username, pass);
            //Presence presence = new Presence(Presence.Type.available);
            //mConnection.sendPacket(presence);
        } catch (XMPPException e) {
            e.printStackTrace();
            return;
        }
        AppContext.commonLog.i("Chat login success");
        AppContext.commonLog.i("Asmack getUser --> " + mConnection.getUser());
    }

    public Chat initChat(String toUserId){
        return AppContext.xmppConnectionManager.getConnection().getChatManager()
                .createChat(toUserId + "@incircle/Smack", null);
    }


    public void send(Chat chat, UserMessage userMessage, String toUserId){
        String jsonUserMessage = JsonUtil.toJson(userMessage);
        send(chat, jsonUserMessage, toUserId, ChatMessageType.USERMESSAGE);
    }

    public void send(Chat chat, Comment comment, String toUserId){
        String jsonComemnt = JsonUtil.toJson(comment);
        send(chat, jsonComemnt, toUserId, ChatMessageType.COMMENT);
    }

    public void send(Chat chat, Praise praise, String toUserId){
        String jsonPraise = JsonUtil.toJson(praise);
        AppContext.commonLog.i(jsonPraise);
        send(chat, jsonPraise, toUserId, ChatMessageType.PRAISE);
    }

    public void send(Chat chat, String content, String toUserId, String chatMessageType){
        if(AppContext.auth != null) {
            AppContext.commonLog.i(content);
            Message message = new Message();
            message.setBody(content);
            message.setType(Message.Type.chat);
            message.setSubject(chatMessageType);
            message.setTo(toUserId + "@" + SERVICE_NAME);
            message.setFrom(AppContext.auth.getId() + "@" + SERVICE_NAME);
            AppContext.commonLog.i(message.toString());
            try {
                chat.sendMessage(message);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    public void configure(ProviderManager pm) {

        // Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private",
                new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time",
                    Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
        }

        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
                new XHTMLExtensionProvider());

        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster",
                new RosterExchangeProvider());
        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event",
                new MessageEventProvider());
        // Chat State
        pm.addExtensionProvider("active",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());

        // FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si",
                new StreamInitiationProvider());

        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference",
                new GroupChatInvitation.Provider());
        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
                new DiscoverItemsProvider());
        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
                new DiscoverInfoProvider());
        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
                new MUCUserProvider());
        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
                new MUCAdminProvider());
        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
                new MUCOwnerProvider());
        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay",
                new DelayInformationProvider());
        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version",
                    Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
        }
        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
                new OfflineMessageRequest.Provider());
        // Offline Message Indicator
        pm.addExtensionProvider("offline",
                "http://jabber.org/protocol/offline",
                new OfflineMessageInfo.Provider());
        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup",
                "http://www.jivesoftware.org/protocol/sharedgroup",
                new SharedGroupsInfo.Provider());
        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses",
                "http://jabber.org/protocol/address",
                new MultipleAddressesProvider());

    }

    public static void main(String[] args){
        String username = "624942";
        String pass = "624942";
        if(XmppConnectionManager.getInstance().init() != null){
            XMPPConnection connection = XmppConnectionManager.getInstance().getConnection();
            try {
                connection.connect();
                connection.login(username, pass);
                Presence presence = new Presence(Presence.Type.available);
                connection.sendPacket(presence);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            System.out.println("login success");
        }
    }

}
