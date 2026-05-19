package com.shraddha.automation;

import java.util.concurrent.atomic.AtomicInteger;

public class TestData {

    private static final AtomicInteger appliedCount = new AtomicInteger(0);
    private static final AtomicInteger skippedCount = new AtomicInteger(0);

    public static void appliedCount() {
        appliedCount.incrementAndGet();
    }

    public static void skippedCount() {
        skippedCount.incrementAndGet();
    }

    public static int getApplied() {
        return appliedCount.get();
    }

    public static int getSkipped() {
        return skippedCount.get();
    }

    public static int getTotal() {
        return appliedCount.get() + skippedCount.get();
    }

    public static void clear() {
        appliedCount.set(0);
        skippedCount.set(0);
    }

    // Profile information for auto-answering verification questions (Based on Resume: Shraddha Vankar)
    static final String CURRENT_CTC = "6.3"; // Lacs (QA Engineer - 4+ years exp)
    static final String EXPECTED_CTC = "9"; // Lacs (Software Engineer level)
    static final String EXPERIENCE_YEARS = "4.3"; // Years (April 2021 - June 2025)
    static final String NOTICE_PERIOD = "Immediate Joiner"; // Days (Immediate joiner)
    static final String CURRENT_COMPANY = "Yagna IQ Pvt Ltd";
    static final String CURRENT_ROLE = "Software Engineer / QA Engineer";
    static final String LAST_WORKING_DAY = "30/06/2025";
    static final String EDUCATION_DEGREE = "B.E. Electronics & Telecommunication";
    static final String EDUCATION_PERCENTAGE = "73.74";
    static final String COLLEGE_NAME = "Jaihind College of Engineering";
    static final String LOCATION = "Pune";
    static final String BIRTH_DATE = "03/03/1996";
    static final String PAN_NUMBER = "CKEPG2632Q";
    static final String[] LANGUAGES = {"English", "Hindi", "Marathi", "Malayalam"};
    static final String[] PREFERRED_WORK_TYPES = {"Work from Home", "Hybrid", "On-site"};
    static final String CERTIFICATIONS = "Certified in Software Testing - Cyber Success Institute";
    static final String OPEN_TO_TRAVEL = "Yes";
    static final String OPEN_TO_RELOCATE = "Yes";
    static final String DOMAIN_EXPERIENCE = "SaaS, Channel Sales Automation, CPQ";
    static final String NO_EXPERIENCE_YEARS = "0";
    static final String LITTLE_EXPERIENCE_YEARS = "1 Year";
    static final String COMMUNICATION_SKILLS = "8/10 (Fluent in English, good verbal and written skills)";
    
    // Radio button / Selection-based answers
    static final String ATTEND_WALK_IN = "No";
    static final String IMMEDIATE_JOINER = "Yes";
    static final String OPEN_TO_WORK = "Yes";
    static final String TITLE = "Mrs.";
    static final String MARITAL_STATUS = "Married";
    static final String NOTICE_PERIOD_RADIO = "15 Days or less";
    static final String YOE_RADIO = "3-5 years";
    static final String NO_EXPERIENCE_RADIO = "No experience";
    static final String LITTLE_EXPERIENCE_RADIO = "1-3 years";

    public static String getAnswerForQuestion(String questionText) {
        String q = questionText.toLowerCase();
        
        // Current CTC questions
        if (q.contains("current ctc") || q.contains("cctc") || q.contains("fixed ctc")  || q.contains("current cost") || q.contains("current salary") || q.contains("current pay") || q.contains("current fixed salary")) {
            return CURRENT_CTC;            
        }
        
        // Expected CTC questions
        if (q.contains("expected ctc") || q.contains("ectc")  || q.contains("expected annual") || q.contains("expected salary") || q.contains("expected cost") || q.contains("expected pay") || q.contains("expected package")) {
            return EXPECTED_CTC;
        }
        
        // Current salary/package questions
        if ((q.contains("current ctc") || q.contains("current salary") || q.contains("current pay") || q.contains("current package")) && !q.contains("expected")) {
            return CURRENT_CTC;
        }
        
        // No Experience questions
        if ((q.contains("experience") || q.contains("exp")) && (q.contains("aws") || q.contains("guidewire") || q.contains("appium") || q.contains("Claude") || q.contains("atf") || q.contains("tosca") || q.contains("cucumber") || q.contains("python") || q.contains("jmeter") || q.contains("rest assured") || q.contains("seeburger") || q.contains("mft") || q.contains("c#") || q.contains("uft") || q.contains("vb script") || q.contains("vbscript") || q.contains("visual basic scripting") || q.contains("samd") || q.contains("ecg") || q.contains("ble") || q.contains("iot") || q.contains("ai") || q.contains("ai/ml") || q.contains("pytest") || q.contains("bdd") || q.contains("azure") || q.contains("mobile") || q.contains("javascript") || q.contains("etl") || q.contains("iso") || q.contains("sepa") || q.contains("sap") || q.contains("oracle") || q.contains("salesforce") || q.contains("servicenow") || q.contains("charging") || q.contains("cybersecurity") || q.contains("networking") || q.contains("interoperability") || q.contains("cloud") || q.contains("devops") || q.contains("ac/dc") || q.contains("ccs") || q.contains("plc") || q.contains("battery") || q.contains("workflow") || q.contains("power cable"))) {
            return NO_EXPERIENCE_YEARS;
        }

        // Little Experience questions
        if ((q.contains("experience") || q.contains("exp")) && (q.contains("playwright") || q.contains("typescript")  || q.contains("sql") || q.contains("cassandra") || q.contains("api"))) {
            return LITTLE_EXPERIENCE_YEARS;
        }

        // generic experience questions
        if ((q.contains("total experience") || q.contains("overall experience") || q.contains("years of experience") || q.contains("exp"))) {
            return EXPERIENCE_YEARS;
        }
    
        // Notice period / Joining questions
        if (q.contains("notice period") || q.contains("notice days") || q.contains("notice board") || q.contains("can you join")) {
            return NOTICE_PERIOD;
        }

        // Last working day questions
        if (q.contains("last working day") || q.contains("last working date") || q.contains("last day") || q.contains("final day")) {
            return LAST_WORKING_DAY;
        }
        
        // Company name questions
        if (q.contains("current company") || q.contains("company name") || q.contains("working at")) {
            return CURRENT_COMPANY;
        }
        
        // Current role/designation questions
        if (q.contains("current role") || q.contains("current position") || q.contains("designation") || q.contains("current job")) {
            return CURRENT_ROLE;
        }
        
        // Education degree questions
        if (q.contains("degree") || q.contains("qualification") || q.contains("educational background") || q.contains("highest education")) {
            return EDUCATION_DEGREE;
        }
        
        // Education percentage/grade questions
        if (q.contains("percentage") || q.contains("cgpa") || q.contains("grade") || q.contains("score")) {
            return EDUCATION_PERCENTAGE;
        }
        
        // College/University name questions
        if (q.contains("college") || q.contains("university") || q.contains("institution")) {
            return COLLEGE_NAME;
        }
        
        // Location/City questions
        if (q.contains("current location") || q.contains("city") || q.contains("where do you") || q.contains("from which city")) {
            return LOCATION;
        }
        
        // Postal code questions
        if (q.contains("postal code") || q.contains("pin code") || q.contains("zip code")) {
            return "411046";
        }

        // Work type/preference questions
        if (q.contains("preferred location") || q.contains("work preference") || q.contains("remote") || q.contains("on-site") || q.contains("hybrid") || q.contains("wfh")) {
            return PREFERRED_WORK_TYPES[0]; // "Work from Home"
        }
        
        // Travel questions
        if (q.contains("travel") || q.contains("business travel")) {
            return OPEN_TO_TRAVEL;
        }
        
        // Relocation questions
        if ((q.contains("relocation") || q.contains("relocate") || q.contains("willing to") || q.contains("ready to move")) && q.contains("pune")) {
            return OPEN_TO_RELOCATE;
        }
        
        // No Relocation questions
        if ((q.contains("relocation") || q.contains("relocate") || q.contains("willing to") || q.contains("ready to move")) && !q.contains("pune")) {
            return "No";
        }
        
        // Language questions
        if (q.contains("language") || q.contains("speak") || q.contains("knowledge")) {
            return String.join(", ", LANGUAGES);
        }
        
        // Birth date questions
        if (q.contains("birth date") || q.contains("date of birth") || q.contains("dob")) {
            return BIRTH_DATE;       
         }
        
        // PAN questions
        if (q.contains("pan")) {
            return PAN_NUMBER;        
        }

        // Certification questions
        if (q.contains("certification") || q.contains("certified") || q.contains("training")) {
            return CERTIFICATIONS;
        }
        
        // Immediate joiner questions
        if (q.contains("immediate") ||q.contains("last working day") || q.contains("Which month you can join") || q.contains("when can you") || q.contains("can you start") || q.contains("lwd")) {
            return "Yes/Immediate joiner";
        }
        
        // Career break or explicitly negative questions
        if (q.contains("career break") || q.contains("break in experience") || q.contains("gap in experience")) {
            return "No";
        }

        // Negative questions explicitly asking 'No'
        if ((q.contains("not") || q.contains("don't") || q.contains("no") || q.contains("previous") || q.contains("relative") || q.contains("offer") || q.contains("have you worked with")) && !q.contains("current") && !q.contains("expected")) {
            return "No";
        }

        // Domain/Industry experience
        if (q.contains("domain") || q.contains("industry") || q.contains("saas")) {
            return DOMAIN_EXPERIENCE;
        }

        // Communication skills
        if (q.contains("communication") || q.contains("verbal")) {
            return COMMUNICATION_SKILLS;
        }

        // General yes/no questions
        if (q.contains("willing") || q.contains("can you") || q.contains("buy out option") || q.contains("are you") || q.contains("would you") || q.contains("okay") || q.contains("fine") || q.contains("agree") || q.contains("consent") || q.contains("accept") || q.contains("ready") || q.contains("playwright")) {
            return "Yes"; // Default positive answer
        }
        
        return ""; // Default: user will input manually
    }
    
    public static String getRadioButtonAnswer(String questionText) {
        String q = questionText.toLowerCase();
        // Title questions
        if ((q.contains("title")) || (q.contains("salutation")))  {
            return TITLE;
        }

        // Gender questions
        if ((q.contains("diversity")) || (q.contains("gender")))  {
            return "Female";
        }

        // Marital status questions
        if ((q.contains("marital")))  {
            return MARITAL_STATUS;
        }

        // Relocation questions
        if ((q.contains("relocation") || q.contains("relocate") || q.contains("willing to") || q.contains("ready to move")) && q.contains("pune")) {
            return "Yes";
        }
        
        // No Relocation questions
        if ((q.contains("relocation") || q.contains("relocate") || q.contains("willing to") || q.contains("ready to move")) && !q.contains("pune")) {
            return "No";
        }
        
        // Negative questions explicitly asking 'No'
        if ((q.contains("not") || q.contains("don't") || q.contains("do not") || q.contains("no")) && (q.contains("python") || q.contains("tosca") || q.contains("previous") || q.contains("relative") || q.contains("offer") || q.contains("ai") || q.contains("have you worked with"))) {
            return "No";
        }

        // Walk-in attendance questions
        if (q.contains("walk-in") || q.contains("walk in") || q.contains("walkin") || q.contains("attend") || q.contains("will you come") || q.contains("walk in attendance") || q.contains("face to face") || q.contains("in person")) {
            return ATTEND_WALK_IN;
        }

        // Immediate joining questions
        if (q.contains("immediate") || q.contains("can you join") || q.contains("can you start") || q.contains("joining") || q.contains("start date") || q.contains("joining date") || q.contains("available from")) {
            return IMMEDIATE_JOINER;
        }

        // Open to work questions
        if (q.contains("open to work") || q.contains("looking for") || q.contains("job search") || q.contains("currently working") || q.contains("available") || q.contains("actively seeking")) {
            return OPEN_TO_WORK;
        }

        // Travel / relocation questions may appear as radio selections
        if (q.contains("travel") || q.contains("business travel") || q.contains("willing to travel") || q.contains("ready to move") || q.contains("legally authorized")) {
            return "Yes";
        }

        // Career break radio questions
        if (q.contains("career break") || q.contains("break in experience") || q.contains("gap in experience") || q.contains("have you taken a break")) {
            return "No";
        }

        // Specific acceptance questions
        if (q.contains("agree") || q.contains("consent") || q.contains("accept")) {
            return "I Accept";
        }

        // General yes/no questions
        if (q.contains("willing") || q.contains("can you") || q.contains("buy out option") || q.contains("are you") || q.contains("would you") || q.contains("okay") || q.contains("fine") || q.contains("ready") || q.contains("playwright")) {
            return "Yes"; // Default positive answer
        }

        // Notice period questions
        if (q.contains("notice") || q.contains("notice period") || q.contains("lwd") || q.contains("last working day")) {
            return NOTICE_PERIOD_RADIO;
        }

        // No Experience questions
        if ((q.contains("experience") || q.contains("exp")) && (q.contains("aws") || q.contains("atf") || q.contains("Claude") || q.contains("tosca") || q.contains("cucumber") || q.contains("python") || q.contains("jmeter") || q.contains("rest assured") || q.contains("seeburger") || q.contains("mft") || q.contains("c#") || q.contains("uft") || q.contains("vb script") || q.contains("vbscript") || q.contains("visual basic scripting") || q.contains("samd") || q.contains("ecg") || q.contains("ble") || q.contains("iot") || q.contains("ai") || q.contains("ai/ml") || q.contains("pytest") || q.contains("bdd") || q.contains("azure") || q.contains("mobile") || q.contains("javascript") || q.contains("etl") || q.contains("iso") || q.contains("sepa") || q.contains("sap") || q.contains("oracle") || q.contains("salesforce") || q.contains("servicenow") || q.contains("charging") || q.contains("cybersecurity") || q.contains("networking") || q.contains("interoperability") || q.contains("cloud") || q.contains("devops") || q.contains("ac/dc") || q.contains("ccs") || q.contains("plc") || q.contains("battery") || q.contains("workflow") || q.contains("power cable"))) {
            return NO_EXPERIENCE_RADIO;
        }
        
        // Little Experience questions
        if ((q.contains("experience") || q.contains("exp")) && (q.contains("playwright") || q.contains("typescript")  || q.contains("sql") || q.contains("cassandra") || q.contains("api"))) {
            return LITTLE_EXPERIENCE_RADIO;
        }

        // work experience questions
        if (q.contains("experience") || q.contains("exp")){            
            return YOE_RADIO;
        }

        return "";
    }
}