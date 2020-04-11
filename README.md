# Quick Customer Managment

A desktop application to manage your customers and create invoices in any currency.
The application is written in Java 13 and is easy to use. 

### Features
- A list of currencies and countries
The list is downloaded through the Java HTTP Package and is available automatically in this application.
- Invoices and customer database
Your data is saved fast and efficient in an SQLlite Database.
- Create and send invoice PDFs with one click
Invoices are saved in PDF in your invoices folder and they are sent to the customer email address through your SMTP mail provider.
- Overview of unpaid invoices
If you start the application, then you can see directly the unpaid invoices with the customer and invoice sum. 
- Create and manage products
You can create product templates and manage the available amount of products and the product price.

![Screenshot of Desktop Application Quick Customer Managment](https://raw.githubusercontent.com/a-dridi/QuickCustomerManagment/master/screenshot.PNG)

The application needs a Java runtime environment with a version of at least 13.0.

## Requirements
These following packages are needed for this application to build it:
- JavaFX 11 SDK (OpenJDK)
- sqlite-jdbc-3.30.1
- bcprov-jdk15on-1.64
- itextpdf-5.5.13.1
- javax.mail-1.6.2.jar
- activation-1.1.1.jar
- httpclient-4.5.12.jar
- httpcore-4.4.13.jar
- commons-logging-1.2.jar
- commons-codec-1.11.jar

(Optional) For unit testing: JUnit5

These can be downloaded from maven central. This project does not use maven repository, but it uses the jar files from there.
If you use this procect folder, then you can import the downloaded jars (from the maven repository) in the settings "Properties" of this project folder by clicking on "Add External JARs".

Httpclient-xxx, httpcore-xxx, commons-logging and commons-codec can be downloaed on this site: 
http://hc.apache.org/downloads.cgi

##Important configuration
Please activate the recommended report error functionality. You can do that by copying the file "credentials.txt_template" to the file "credentials.txt" with the values of your error report server.

## Test
A JUnit Test (JUnit5) is available in the file: src/test/AppDataSettingsTest.java

## Run
There is a runnable jar file in this root github repository. 
If you want to compile this project, then check the Main application is in the file and package QuickCustomerManagment/Main.Java

##Installation
Besides the executable (exe file), there is also an installer for Windows. It can be downloaded from the release page:
https://github.com/a-dridi/QuickCustomerManagment/releases

## Custom Installation (Deployment)
- Option 1: 
You can create an exe file of the runnable jar file in this root github repository by using the program "launch4j". Please use for that the file "quickcustomermanagment.xml".

- Option 2:
Deployment from source. Please download this repository and open it in an IDE to build and run this application. Preferably "Eclipse", because this application folder was created in that IDE.

## Authors

* **A. Dridi** - [a-dridi](https://github.com/a-dridi/)

