
package yooha;

import yooha.network.Connection;


public class MessageStringPrinter implements MessageStringHandler
{

    public void handleMessageString( String messageString, Connection conn )
    {
        System.out.println(messageString);
    }
}
