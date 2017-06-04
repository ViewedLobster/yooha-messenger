package yooha;
public class MainModel {
	private String nick;
	private String clan;
	private int listeningPort;
	
	public MainModel(String nickIn, String clanIn, int listeningPortIn){
		nick = nickIn;
		clan = clanIn;
		listeningPort = listeningPortIn;
	}
	
	public String getNick(){
		return nick;
	}
	
	public String getClan(){
		return clan;
	}
	
	public int getListeningPort(){
		return listeningPort;
	}
}
