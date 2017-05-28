package yooha;

import yooha.network.Connection;


public class ClientBackend implements ChatBackend
{
    private Connection conn;

    public ClientBackend( Connection conn ){
        this.conn = conn;

    }

    public void sendMessage( Message message )
    {


    }

}
