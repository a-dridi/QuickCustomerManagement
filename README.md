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

## Test
A JUnit Test (JUnit5) is available in the file: src/test/AppDataSettingsTest.java

## Run
Main application is in the file and package QuickCustomerManagment/Main.Java

## Installation (Deployment)
Please download this repository and open it in an IDE to build and run this application. Preferably "Eclipse", because this application folder was created in that IDE.

## Authors

* **A. Dridi** - [a-dridi](https://github.com/a-dridi/)

