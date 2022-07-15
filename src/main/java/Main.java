import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {


        //String patch1 = "src/main/resources/myBase.txt";
        // patch1 - link to your txt file
        // patch1 - Это сылка на ваш те txt фай
        // patch1 - Це посилання на ваш txt файл
        //workMailEndPassword(patch1);

//        String patchMyAccount = "Link you txt file";
//        String patchSpamEmail = "Link you txt file";
//        emailMessage(patchMyAccount, patchSpamEmail);

//        checkSizeTxtFile("link you txt file");
    }

    public static void workMailEndPassword(String path){
        try {
            outputFile(ConvertTextFile(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //getting data from a text file and adding it to an ArrayList.
    //получение данных из текстового файла и добавление их в ArrayList.
    //отримання інформації з текстового файла і додавання її в ArrayList.
    public static ArrayList<String> ConvertTextFile(String patch) throws IOException {

        File importFile = new File(patch);
        Scanner scr = new Scanner(importFile);
        ArrayList<String> information = new ArrayList<>();

        while (scr.hasNextLine()){
            information.add(scr.nextLine());
        }

        scr.close();
        return information;
    }

    //The method that will separate the username and password, there should be a sign between them - :
    //Метод, который будет разделять имя пользователя и пароль, между ними должен быть знак - :
    //метод який розділяє імя користувача і його пароль, між ними має бути знак - :
    public static void outputFile(ArrayList<String> information) throws IOException {
        FileWriter fileGmail = null;
        //FileWriter filePassword = null;
        //For some reason, after 700 entries, the file throws an ArrayIndexOutOfBoundsException error, I'll figure it out later.
        //Файл почему-то после 700 записи выдает ошибку ArrayIndexOutOfBoundsException, позже  разберусь.
        //Файл чомусь після 700 запису видає помилку ArrayIndexOutOfBoundsException, пізніше розберусь.

        try {
            fileGmail = new FileWriter("outputEmail.txt");
            //filePassword = new FileWriter("outputPassword.txt");

            for(String email : information){

                String[] setChar = (String[]) email.split(":");
                //Here you can specify your own dividing sign.
                //Здесь вы можете указать свой собственный разделительный знак.
                //Тут ви можете вказати свій знак, який буде розділяти строку.

                String v1 = Objects.toString(setChar[0]);
                //String v2 = Objects.toString(setChar[1]);

                fileGmail.write(v1 + System.getProperty("line.separator"));
                //filePassword.write(v2 + System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fileGmail.close();
            //filePassword.close();
        }

    }


    //Unfortunately, from May 20, 2022, Google will disable the ability to manage "Access to less secure applications".
    //When I find a new way, I will definitely update the program.

    //К сожалению с 20 мая 2022 года гугл отключит возможность управлять "Доступ к менее безопасным приложениям".
    //Когда найду новый способ, то обязательно обновлю программу.

    //На жаль з 20 травня 2022 року гугл відключить можливість управляти "Доступ до менш безпечних додатків".
    //Коли знайду новий спосіб, то обов'язково оновлю програму.

    public static void emailMessage(String patchMyAccount, String patchSpamEmail){
        try {
            getEmailTxt(patchMyAccount, patchSpamEmail);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }


    private static void getEmailTxt(String patchMyAccount, String patchSpamEmail) throws IOException, MessagingException {

        ArrayList<String> myAccount = ConvertTextFile(patchMyAccount);
        ArrayList<String> spamEmail = ConvertTextFile(patchSpamEmail);

        ArrayList<String> myAccountPassword = new ArrayList<>();
        ArrayList<String> myAccountLogin = new ArrayList<>();

        if(myAccount.size() != 0 && spamEmail.size() != 0){

            for(String email : myAccount){

                String[] setChar = (String[]) email.split(":");

                myAccountLogin.add(setChar[0]);
                myAccountPassword.add(setChar[1]);
            }

            spamEmail(myAccountLogin, myAccountPassword, spamEmail);
        }
    }


    private static void spamEmail(ArrayList<String> myLogin, ArrayList<String> myPassword, ArrayList<String> spamEmail) throws MessagingException {

        if(myLogin.size() != myPassword.size()){
            System.out.println("login size != password size");
        }else {

            int messagesFromOneEmail = 0;

            if(spamEmail.size() / myLogin.size() <= 0){
                messagesFromOneEmail = 1;
            }else{
                messagesFromOneEmail = spamEmail.size() / myLogin.size();
            }

            for (int i = 0; i <= spamEmail.size() - 1; i++) {
                String myLoginEmail = myLogin.get(i);
                String myPasswordEmail = myPassword.get(i);

                for (int j = 0; j == messagesFromOneEmail - 1; j++) {
                    String spamAccountEmail = spamEmail.get(j);
                    pushMessageEmail(myLoginEmail, myPasswordEmail, spamAccountEmail);
                }
            }
        }
    }

    //In the Google account settings, you need to enable "Access to less secure applications"(2fa should be disabled).
    //В настройках акаунта гугл нужно включить "Доступ к менее безопасным приложениям"(2fa должен быть отключен).
    //В настройках акаунта гугл потрібно включити "Доступ для менш безпечних додатків" (2fa повинен бути виключеним).
    private static void pushMessageEmail(String myEmail, String myPassword, String pushEmail) throws MessagingException {

        String host = "smtp.gmail.com";

        Properties props = System.getProperties();
        props.put ("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", myEmail);
        props.put("mail.smtp.password", myPassword);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        String[] to = {pushEmail};

        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myEmail));

        InternetAddress[] toAddress = new InternetAddress[to.length];

        for (int i = 0; i <to.length; i ++) {
            toAddress[i] = new InternetAddress(to[i]);
        }
        System.out.println(Message.RecipientType.TO);

        for (int i = 0; i <toAddress.length; i ++) {
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
        }

        message.setSubject("WARNING!!!");
        message.setText("you message");

        Transport transport = session.getTransport("smtp");
        transport.connect(host, myEmail, myPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }


    //A method by which you can find out the number of lines in your text file.
    //Метод, с помощью которого вы можете узнать количество строк в вашем текстовом файле.
    //Метод за допомогою якого ви можете узнати кількість строк у вашому текстовому файлі.
    private static void checkSizeTxtFile(String patch) throws FileNotFoundException {
        File importFile = new File(patch);
        Scanner scr = new Scanner(importFile);
        ArrayList<String> information = new ArrayList<>();

        while (scr.hasNextLine()){
            information.add(scr.nextLine());
        }

        System.out.println(information.size());
        scr.close();
    }

}


