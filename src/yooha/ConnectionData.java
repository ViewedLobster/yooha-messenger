package yooha;

public class ConnectionData
{

    public String nick;
    public String clan;
    public final int connectionId;
    

    public ConnectionData ( String nick, String clan, int connectionId )
    {
        this.nick = nick;
        this.clan = clan;
        this.connectionId = connectionId;
    }
}
