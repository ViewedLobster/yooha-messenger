
package yooha;


public class MessageStringPrinter implements MessageStringHandler
{

    public void handleMessageString( String messageString, int connectionId )
    {
        System.out.println(messageString);
    }
}
