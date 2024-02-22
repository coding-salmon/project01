package dbTest;

import java.io.IOException;
import java.util.Scanner;

public class Payment1 {
	// 결제 메서드 추가
    public static void payment(int point, int price) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int payChoice = 0;
        do {
            String pay;
            System.out.println("결제 수단을 선택해주세요:");
            System.out.println("1. 스타벅스 카드");
            System.out.println("2. 신용카드");
            System.out.println("3. 쿠폰");
            payChoice = scanner.nextInt();
            switch (payChoice) {
                case 1:
                    pay = "스타벅스 카드";
                    System.out.println(pay+ "결제가 완료되었습니다.");
                    point -= price; // 커피 가격만큼 포인트 차감
                    System.out.println("잔액 : " + point);
                    break;
                case 2:
                    pay = "신용카드";
                    System.out.println(pay+ "결제가 완료되었습니다.");
                    break;
                case 3:
                    pay = "쿠폰";
                    System.out.println(pay+ "결제가 완료되었습니다.");
                    break;
                default:
                    System.out.println("잘못된 선택입니다.");
                    scanner.nextLine();
                    break;
            }
        } while (payChoice != 1 && payChoice != 2 && payChoice != 3);
    }
}
