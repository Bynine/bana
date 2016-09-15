package entities;

public class Wallet {
	int money;
	Wallet() { money = 0; }
	public int getMoney() { return money; }
	public void addMoney(int add){ money += add; }
	public void empty(){ money = 0; }
}
