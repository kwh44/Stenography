import java.util.Scanner;

public class Main {
    public static void main(String argc[]) {
        String message;
        String image_to_hide_message_path;

        StegHide steghide = new StegHide();
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter the message to hide: ");
        message = scan.nextLine();

        System.out.print("Enter the path to image in which to hide the message: ");
        image_to_hide_message_path = scan.nextLine();

        steghide.embed(message, image_to_hide_message_path);
        steghide.extract(image_to_hide_message_path);
    }
}
