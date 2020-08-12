package com.geurimsoft.gmessenger.data;

public class UserList {
    private String jid;

    public UserList(String contactJid)
    {
        jid = contactJid;
    }

    public String getJid()
    {
        return jid;
    }

    public void setJid(String jid) { this.jid = jid; }
}
