package de.thm.asteroidshooter.Connection;

/*
* Bestimmt die Commands fur die Kommunikation zwischen Games und Infrastruktur
*/
public interface MOWSchnittstellenInterface {

	public final static String SERVICE_PACKAGE = "de.thm.mps"; 
	
    /**
    * Nachricht I
    */

    //what:
    public final static int MESSAGE_I_REGISTER = 1; // Beim Service anmelden


    /**
    * Nachricht II
    */

    //what:
    public final int MESSAGE_2_CONNECTION=2;
    //Bundle der Nachricht II enthalt folgende Keys:
    public final static String CONNECTION_PLAYERCOUNT="message2.player.count"; // Gibt int zuruck
    public final static String CONNECTION_PLAYERNAMES="message2.player.names"; //Gibt String zuruck, Player per ; getrennt
    public final static String CONNECTION_OWN_ID="message2.self.id"; //Gibt Int der ID zuruck
    public final static String CONNECTION_ACTIVITY="message2.activity"; //Gibt String zuruck in der Form activityname

    /**
    *Nachricht III
    */

    //what:
    public final int MESSAGE_3_SEND=3;
    //Bundle der Nachricht III enthalt folgende Keys:
    public final static String SEND_ADDRESS="message3.address.id"; // Gibt int zuruck
    public final static String SEND_TYPE="message3.datatype"; //Gibt String zuruck
    public final static String SEND_DATA="message3.data"; //Gibt Rohdaten zuruck

    /**
    * Nachricht IV
    */

    //what:
    public final int MESSAGE_4_GET=4;
    //Bundle der Nachricht IV enthalt folgende Keys:
    public final static String GET_ADDRESS="message4.address.id"; // Gibt int zuruck
    public final static String GET_TYPE="message4.datatype"; //Gibt String zuruck
    public final static String GET_DATA="message4.data"; //Gibt Rohdaten zuruck

    /**
    * Nachricht V
    */

    //what:
    public final int MESSAGE_5_CLOSE=5;

    /**
    * Nachricht VI
    */

    //what:
    public final int MESSAGE_6_CLOSED=6;
    //Bundle der Nachricht V enthalt folgende Keys:
    public final static String CLOSED_BY = "closed.id"; // Liefert int zuruck

}