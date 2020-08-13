/**
 * UserList.class
 * 기능 : 유저 데이터 저장 클래스
 */

package com.geurimsoft.gmessenger.data;

public class UserList {
    private String jid;

    /**
     * @func UserList 생성자
     * @param contactJid
     * @return
     */
    public UserList(String contactJid) {
        jid = contactJid;
    }

    /**
     * @func UserList getter
     * @param
     * @return jid
     */
    public String getJid() {
        return jid;
    }

    /**
     * @func UserList setter
     * @param jid
     * @return
     */
    public void setJid(String jid) {
        this.jid = jid;
    }
}
