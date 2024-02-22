package sirenorder;

//import java.io.IOException;
//import java.util.Scanner;

public class Payment {
	private String userId;
	public static int money = 10000;

	public Payment(String userId, int money) {
		this.userId = userId;
		this.money = money;
	}

	public String getUserId() {
		return userId;
	}
	
	public int savedMoney() { // 추후에 잔액 확인 기능 구현 가능
		return money;
	}
	
	public static int leftMoney(int price) {
		money -= price;
		return money;
	}
}
