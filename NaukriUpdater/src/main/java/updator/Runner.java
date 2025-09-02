package updator;

public class Runner {

	public static void main(String[] args) throws InterruptedException {
		Updator obj = new Updator();
		obj.loginToNaukri();
		obj.updateName();
		obj.tearDown();

	}

}
