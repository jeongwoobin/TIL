/**
 * UserList.class
 * 기능 : 유저 데이터 저장 클래스
 */

package com.geurimsoft.gmessenger.data;

public class UserList
{

    private String jid;

    /**
     * UserList 생성자
     * @param contactJid 유저 id 추가
     */
    public UserList(String contactJid)
    {

        jid = contactJid;
    }

    /**
     * UserList getter
     * @return String 유저 id
     */
    public String getJid()
    {

        return jid;
    }

    /**
     * UserList setter
     * @param jid 유저 id 세팅
     */
    public void setJid(String jid)
    {

        this.jid = jid;
    }
}
