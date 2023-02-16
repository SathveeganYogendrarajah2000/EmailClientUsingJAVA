package com.emailS_;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.emailS_.Email_Client.currentDay;

///////////////       200592R       ///////////////
//////////    Sathveegan Yogendrarajah    //////////
public class Email_Client {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public static String currentDay = formatter.format(LocalDate.now());

    private static final ArrayList<eMail> mailList =  new ArrayList<>();
    private static final ArrayList<String> autoBdayWishList = new ArrayList<>();


    public static void main(String[] args) throws IOException, MessagingException {

        ArrayList<Recipient> recList;

        File clientListFile = new File("clientList.txt");
        clientListFile.createNewFile();

        recList = readingFile(clientListFile);

        //Object deserialization.
        File objectFile = new File("objectList.txt");
        objectFile.createNewFile();
        if(objectFile.length() != 0) deserializing(objectFile,mailList);

        autoSendBdayWishes(recList);

        Scanner scanner = new Scanner(System.in);

        String help = "Enter option type: \n"
                + "1 - Adding a new recipient\n"
                + "2 - Sending an email\n"
                + "3 - Printing out all the recipients who have birthdays\n"
                + "4 - Printing out details of all the emails sent\n"
                + "5 - Printing out the number of recipient objects in the application\n"
                + "-1 - Exit.\n"
                + "0 - Need help?";

        System.out.println(help);

        String option = scanner.nextLine();
        while(!Objects.equals(option, "-1")) {
            switch (option) {
                case "0":
                    System.out.println(help);
                    break;

                case "1":
                    System.out.println("Please enter your Recipient's details in the next line.\nInput format: \n" +
                            "           1. Official: nimal,nimal@gmail.com,ceo\n" +
                            "           2. Office_friend: kamal,kamal@gmail.com,clerk,2000/12/12\n" +
                            "           3. Personal: sunil,<nick-name>,sunil@gmail.com,2000/10/10");
                    String newRecipient = scanner.nextLine();

                    Recipient newRec = makeRecObject(newRecipient);

                    if (newRec != null) {
                        recList.add(newRec);
                        writingFile(clientListFile, newRecipient);
                        System.out.println("Recipient list updated!");
                    }else System.out.println("Wrong Input Format!");

                    // input format - Official: nimal,nimal@gmail.com,ceo
                    // Use a single input to get all the details of a recipient
                    // code to add a new recipient
                    // store details in clientList.txt file
                    // Hint: use methods for reading and writing files

                    break;

                case "2":
                    System.out.println("Please enter Receiver E-Mail and Messages (email, subject, content)");
                    String mailDetails = scanner.nextLine();

                    System.out.println("Preparing to send Email...");

                    String[] mailDetailList = mailDetails.split(",");

                    sendMail(mailDetailList[0],mailDetailList[1],mailDetailList[2]);

                    // input format - email, subject, content
                    // code to send an email
                    System.out.println("Message sent!");

                    break;

                case "3":
                    System.out.print("Please enter date (yyyy/MM/dd): ");
                    String date = scanner.nextLine();

                    String[] dateArr = date.split("/");
                    String dayMonth = dateArr[1] + dateArr [2];

                    for (Recipient recipient : recList) {
                        if (recipient.getClass() == Office_friend.class &&
                                Objects.equals(((Office_friend) recipient).getB_DayAndMonth(), dayMonth)) {
                            System.out.println(recipient.getName());

                        } else if (recipient.getClass() == Personal.class &&
                                Objects.equals(((Personal) recipient).getB_DayAndMonth(), dayMonth)) {
                            System.out.println(recipient.getName());
                        }
                    }
                    System.out.println("No more Recipients!");
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print recipients who have birthdays on the given date

                    break;

                case "4":
                    System.out.println("Please enter date in next line (yyyy/MM/dd): ");
                    String mailDate = scanner.nextLine();

                    if(!mailList.isEmpty()) {
                        for (eMail eMail : mailList) {
                            if (Objects.equals(eMail.getDate(), mailDate)) {
                                System.out.println("Recipient : " + eMail.getRecipient() + "\n" +
                                        "Subject   : " + eMail.getSubject());
                            }
                        }
                    }
                    System.out.println("No more mails to show!");
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print the details of all the emails sent on the input date

                    break;

                case "5":
                    System.out.println("Number of recipient objects: " + Recipient.getCount());
                    // code to print the number of recipient objects in the application

                    break;

                default:
                    System.out.println("Incorrect Option!");
                    break;
            }
            System.out.println("\nNeed help? : Enter '0'.");
            System.out.println("Please select another option:");

            option = scanner.nextLine();
        }

        serializing(objectFile,mailList);

        System.out.println("\nApplication Closed!\nThank you!");
        // start email client
        // code to create objects for each recipient in clientList.txt
        // use necessary variables, methods and classes

    }

    //Create object of recipient from string
    private static Recipient makeRecObject(String r){
        String[] a = null;
        String[] b = null;

        if(r.contains(":")) {
            a = r.split(":");
            b = a[1].split(",");
        }

        Recipient newRec = null;
        if(a == null) return null;

        switch (a[0].toLowerCase()) {
            case "official":
                if (b.length == 3)
                    newRec = new Official(b[0].trim(), b[1].trim(), b[2].trim());
                break;
            case "office_friend":
                if (b.length == 4)
                    newRec = new Office_friend(b[0].trim(), b[1].trim(), b[2].trim(), b[3].trim());
                break;
            case "personal":
                if (b.length == 4)
                    newRec = new Personal(b[0].trim(), b[1].trim(), b[2].trim(), b[3].trim());
                break;
        }
        return newRec;
    }

    //For write in txt file.
    private static void writingFile(File file, String msg) throws IOException {
        FileWriter f = new FileWriter(file, true);
        f.write(msg);
        f.write("\n");
        f.close();
    }

    //Read data from file stores as a list
    private static ArrayList<Recipient> readingFile(File file) throws FileNotFoundException {
        ArrayList<Recipient> recList = new ArrayList<>();
        Scanner read = new Scanner(file);

        while (read.hasNext()){
            String temp = read.nextLine();
            Recipient rec = makeRecObject(temp);
            recList.add(rec);
        }
        return recList;
    }

    //Create string to eMail object and code to send eMail.
    private static void sendMail(String eMailAddress, String subject, String content) throws MessagingException {
        eMail mail = new eMail(eMailAddress,subject,content);

        String userName = "temp@gmail.com";
        String password ="temp123";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        Message message = prepareMessage(session, userName, mail);

        Transport.send(message);
        mailList.add(mail);
    }

    private static Message prepareMessage(Session session, String userName, eMail mail) {
        Message message = null;
        try {
            message = new MimeMessage(session);

            message.setFrom(new InternetAddress(userName));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getRecipient()));
            message.setSubject(mail.getSubject());
            message.setText(mail.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    private static void sendBirthDayWish(Recipient recipient, File bDayFile) throws MessagingException, IOException {
        eMail tempMail = null;

        String[] currentDayArr = currentDay.split("/");
        String currentDayMonth = currentDayArr[1] + currentDayArr [2];

        if(recipient.getClass() == Personal.class &&
                Objects.equals(((Personal) recipient).getB_DayAndMonth(), currentDayMonth))
        {

            tempMail = new eMail(recipient.get_eMail(),
                    ((Personal) recipient).getSubject(),
                    ((Personal) recipient).getB_DayMsg());

        }else if(recipient.getClass() == Office_friend.class &&
                Objects.equals(((Office_friend) recipient).getB_DayAndMonth(), currentDayMonth))
        {
            tempMail = new eMail(recipient.get_eMail(),
                    ((Office_friend) recipient).getSubject(),
                    ((Office_friend) recipient).getB_DayMsg());
        }

        if(tempMail != null){
            sendMail(tempMail.getRecipient(),tempMail.getSubject(),tempMail.getContent());
            writingFile(bDayFile, tempMail.getRecipient());
            autoBdayWishList.add(tempMail.getRecipient());
        }
    }

    private static void autoSendBdayWishes(ArrayList<Recipient> recipientArrayList) throws MessagingException, IOException {

        //Getting details of recipients whose wishes were already sent to avoid repetition of sending birthday wishes.
        File bDayFile = new File("birthDayWishList.txt");
        bDayFile.createNewFile();
        Scanner scan = new Scanner(bDayFile);
        while(scan.hasNext()) autoBdayWishList.add(scan.nextLine());

        for(Recipient i: recipientArrayList){
            if(!autoBdayWishList.contains(i.get_eMail())) {
                sendBirthDayWish(i, bDayFile);
            }
        }
    }

    private static void serializing(File fileName, ArrayList<eMail> arrayList) throws IOException {
//        File file = new File("objectList.txt");

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        for(eMail i : arrayList){
            objectOutputStream.writeObject(i);
            objectOutputStream.flush();
        }
        objectOutputStream.close();
        fileOutputStream.close();
    }

    private static void deserializing(File objectFile, ArrayList<eMail> arrayList) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(objectFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        while(true){
            try {
                eMail eMail = (eMail) objectInputStream.readObject();
                arrayList.add(eMail);

            } catch (EOFException | ClassNotFoundException e) {
                break;
            }
        }
    }
}

interface BirthDayWish {
    String getB_DayAndMonth();
    String getB_DayMsg();
    String getSubject();

}

class eMail implements Serializable {
    private final String recipient;
    private final String subject;
    private final String content;
    private final String date;

    public eMail(String recipient, String subject, String content) {
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        this.date = currentDay;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}

abstract class Recipient {
    private String name;
    private String eMail;
    private static int count = 0;

    public Recipient(String name, String eMail) {
        this.name = name;
        this.eMail = eMail;
        count++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_eMail() {
        return eMail;
    }

    public void set_eMail(String eMail) {
        this.eMail = eMail;
    }

    public static int getCount() {
        return count;
    }
}

class Official extends Recipient {
    private String designation;

    public Official(String name, String eMail, String designation) {
        super(name, eMail);
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}


class Office_friend extends com.emailS_.Official implements BirthDayWish {
    private String birthDay;

    public Office_friend(String name, String eMail, String designation, String birthDay) {
        super(name, eMail, designation);
        this.birthDay = birthDay;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String getB_DayAndMonth() {
        String[] yearMonthDay = this.birthDay.split("/");
        return yearMonthDay[1]+yearMonthDay[2];
    }

    @Override
    public String getB_DayMsg() {
        return "Wish you a Happy Birthday.\nSathveegan.";
    }

    @Override
    public String getSubject() {
        return "Birthday Wish.";
    }
}

class Personal extends com.emailS_.Recipient implements BirthDayWish {
    private String nickName;
    private String birthDay;

    public Personal(String name, String nickName, String eMail, String date) {
        super(name, eMail);
        this.nickName = nickName;
        birthDay = date;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String getB_DayAndMonth() {
        String[] yearMonthDay = this.birthDay.split("/");
        return yearMonthDay[1]+yearMonthDay[2];
    }

    @Override
    public String getB_DayMsg() {
        return "Hugs and Love on your Birthday.\nSathveegan.";
    }

    @Override
    public String getSubject() {
        return "Birthday Wish.";
    }
}
