# EmailClientUsingJAVA
Email Client using SMTP through JAVA

The email client has two types of recipients, official and personal. Some official recipients are close friends.

Details of the recipient list should be stored in a text file.  An official recipient’s record in the text file has the following format: official: <name>, <email>,<designation>. A sample record for official recipients in the text file looks as follows:

Official: nimal,nimal@gmail.com,ceo

A sample record for official friends in the text file looks as follows (last value is the recipient's birthday):

Office_friend: kamal,kamal@gmail.com,clerk,2000/12/12

A sample record for personal recipients in the text file looks as follows (last value is the recipient's birthday):

Personal: sunil,<nick-name>,sunil@gmail.com,2000/10/10

The user should be given the option to update this text file, i.e. the user should be able to add a new recipient through command-line, and these details should be added to the text file. [file handling will be covered in the 11th week]

When the email client is running, an object for each email recipient should be maintained in the application. For this, you will have to load the recipient details from the text file into the application. For each recipient having a birthday, a birthday greeting should be sent on the correct day. Official friends and personal recipients should be sent different messages (e.g. Wish you a Happy Birthday. <your name> for an office friend, and hugs and love on your birthday. <your name> for personal recipients). But all personal recipients receive the same message, and all office friends should receive the same message.  A list of recipients to whom a birthday greeting should be sent is maintained in the application, when it is running. When the email client is started, it should traverse this list, and send a greeting email to anyone having their birthday on that day.

The system should be able to keep a count of the recipient objects. Use static members to keep this count.

All the emails sent out by the email client should be saved into the hard disk, in the form of objects – object serialization can be used for this. The user should be able to retrieve information of all the mails sent on a particular day by using a command-line option.
