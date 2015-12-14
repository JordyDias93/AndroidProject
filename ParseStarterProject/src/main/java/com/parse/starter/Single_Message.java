package com.parse.starter;

import java.util.Date;

/**
 * Created by Dr.Dias on 13/12/2015.
 */
public class Single_Message {

    /** The Constant STATUS_SENDING. */
    public static final int STATUS_RECEIVE = -1;

    /** The Constant STATUS_SENDING. */
    public static final int STATUS_SENDING = 0;

    /** The Constant STATUS_SENT. */
    public static final int STATUS_SENT = 1;

    /** The Constant STATUS_READ. */
    public static final int STATUS_READED = 2;

    /** The Constant STATUS_FAILED. */
    public static final int STATUS_FAILED = 3;

    //Message
    public String msg;

    //Statut
    public int status = STATUS_SENT;

    //Date
    public Date date;

    //L'envoyeur
    public String sender;

    public Single_Message(String msg, Date date, String sender)
    {
        this.msg = msg;
        this.date = date;
        this.sender = sender;
    }

    public Single_Message()
    {
    }

}
