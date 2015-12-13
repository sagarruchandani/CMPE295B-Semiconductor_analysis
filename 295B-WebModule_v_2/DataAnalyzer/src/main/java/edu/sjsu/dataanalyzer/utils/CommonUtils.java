package edu.sjsu.dataanalyzer.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import edu.sjsu.dataanalyzer.bean.ProcessStatus;

public class CommonUtils {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	private static final String filepath = System.getProperty("user.home");
	private static Map<String, Queue<ProcessStatus>> logstore = new HashMap<String, Queue<ProcessStatus>>(); 
	
	public static String storeToFileSystem(MultipartFile file){	
		long timeStamp = Calendar.getInstance().getTimeInMillis();
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream =
						new BufferedOutputStream(new FileOutputStream(new File(filepath+"/"+timeStamp+"_"+file.getOriginalFilename())));
				stream.write(bytes);
				stream.close();
				
				logger.info("file successfully uploaded");
				return filepath+"/"+timeStamp+"_"+file.getOriginalFilename();
			} catch (Exception e) {	
				e.printStackTrace();
				return "error";
			}
		} else {
			return "error";
		}
	}
	
	public static boolean isNumeric(String str){  
		try{  
			Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe){  
			logger.info("metadata field is a valid string");
			return false;  
		}
		logger.info("metadata field is a number / create dummy fields");
		return true;  
	}
	
	public static ArrayList<String> iterateForMetadata(String[] metadata, boolean metadataAvailable){
		ArrayList<String> build = new ArrayList<String>();
		int counter = 0;
		logger.info("iterate over metadata arr : "+metadata.length);
		while(counter < metadata.length){
			if(!metadataAvailable){
				if(counter == metadata.length-1){
					//builder.append("\"feature"+counter+"\":\"true\"");
					build.add("feature"+counter);
				}else{
					build.add("feature"+counter);
				}
			}else{
				if(counter == metadata.length-1){
					build.add(metadata[counter]);
				}else{
					build.add(metadata[counter]);
				}
			}
			counter++;
		}
		return build;
	}
	
	public static ArrayList<String> generateMetadata(File file){
		ArrayList<String> metadataStr2 = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = br.readLine()) != null){
				String[] metadata = line.split(" ");
				if(metadata.length < 2){
					metadata = line.split(",");
				}
				if(metadata.length < 1){
					logger.info("split by space failed");
					ArrayList<String> metadataError = new ArrayList<String>();
					metadataError.add("ERROR");
					return metadataError;
				}else{
					if(isNumeric(metadata[0])){
						metadataStr2 = iterateForMetadata(metadata, false);
					}else{
						metadataStr2 = iterateForMetadata(metadata, true);
					}
				}
				break;
			}
			br.close();
			return metadataStr2;
		} catch (IOException e) {
			ArrayList<String> metadataError = new ArrayList<String>();
			metadataError.add(e.toString());
			e.printStackTrace();
			return metadataError;
		}
	}
	
	public static void setConsoleLog(String uuid, String type, String message){
		ProcessStatus ps= new ProcessStatus(type, message, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
		if(logstore.containsKey(uuid)){
			Queue<ProcessStatus> logQ = logstore.get(uuid);
			logQ.add(ps);
		   logstore.put(uuid, logQ);	
		}else{
			Queue<ProcessStatus> logQ = new LinkedList<ProcessStatus>();
			logQ.add(ps);
			logstore.put(uuid, logQ);
		}
	}
	
	public static List<String> getConsoleLog(String uuid){
		if(logstore.containsKey(uuid)){
			Queue<ProcessStatus> logQ = logstore.get(uuid);
			List<String> logs = new ArrayList<String>();
			while(!logQ.isEmpty()){
				logs.add(logQ.remove().toString());
			}	
			return logs;
		}
		return null;
	}
	
	
	
	
	public static void runFlow(String flow, String uuid, String inputColumns, String outputColumns, String original_data_path, String NUMBER_OF_FEATURES, String SPLIT_TYPE,String TRAIN_SPLIT_RATIO, String OUTPUT_TYPE){
//		 var graphkeys = [{"key":-1,"text":"Comment", "loc":"70 -500"},{"key":-2,"text":"Boosted Decision Tree", "loc":"70 -500"}
//		    ,{"key":-3,"text":"Decision Tree", "loc":"70 -600"},{"key":-4,"text":"Gradient Boosting", "loc":"70 -600"}
//		    ,{"key":-5,"text":"Logistic Regression", "loc":"70 -600"},{"key":-6,"text":"Pearson Correlation", "loc":"70 -600"}
//		    ,{"key":-7,"text":"PCA", "loc":"70 -500"},{"key":-8,"text":"Dataset", "loc":"70 -700"}
//		    ,{"key":-9,"text":"Feature Selection", "loc":"70 -650"},{"key":-10,"text":"Parameter Setting", "loc":"100 -650"}];
		   
		
		// console message type: error, result, status
		
		//String Algorithm = "BOOSTED_DECISION_TREE";
		//String inputColumns="Sensor#1,Sensor#2,Sensor#3,Sensor#4,Sensor#5,Sensor#6,Sensor#7,Sensor#8,Sensor#9,Sensor#10,Sensor#11,Sensor#12,Sensor#13,Sensor#14,Sensor#15,Sensor#16,Sensor#17,Sensor#18,Sensor#19,Sensor#20,Sensor#21,Sensor#22,Sensor#23,Sensor#24,Sensor#25,Sensor#26,Sensor#27,Sensor#28,Sensor#29,Sensor#30,Sensor#31,Sensor#32,Sensor#33,Sensor#34,Sensor#35,Sensor#36,Sensor#37,Sensor#38,Sensor#39,Sensor#40,Sensor#41,Sensor#42,Sensor#43,Sensor#44,Sensor#45,Sensor#46,Sensor#47,Sensor#48,Sensor#49,Sensor#50,Sensor#51,Sensor#52,Sensor#53,Sensor#54,Sensor#55,Sensor#56,Sensor#57,Sensor#58,Sensor#59,Sensor#60,Sensor#61,Sensor#62,Sensor#63,Sensor#64,Sensor#65,Sensor#66,Sensor#67,Sensor#68,Sensor#69,Sensor#70,Sensor#71,Sensor#72,Sensor#73,Sensor#74,Sensor#75,Sensor#76,Sensor#77,Sensor#78,Sensor#79,Sensor#80,Sensor#81,Sensor#82,Sensor#83,Sensor#84,Sensor#85,Sensor#86,Sensor#87,Sensor#88,Sensor#89,Sensor#90,Sensor#91,Sensor#92,Sensor#93,Sensor#94,Sensor#95,Sensor#96,Sensor#97,Sensor#98,Sensor#99,Sensor#100,Sensor#101,Sensor#102,Sensor#103,Sensor#104,Sensor#105,Sensor#106,Sensor#107,Sensor#108,Sensor#109,Sensor#110,Sensor#111,Sensor#112,Sensor#113,Sensor#114,Sensor#115,Sensor#116,Sensor#117,Sensor#118,Sensor#119,Sensor#120,Sensor#121,Sensor#122,Sensor#123,Sensor#124,Sensor#125,Sensor#126,Sensor#127,Sensor#128,Sensor#129,Sensor#130,Sensor#131,Sensor#132,Sensor#133,Sensor#134,Sensor#135,Sensor#136,Sensor#137,Sensor#138,Sensor#139,Sensor#140,Sensor#141,Sensor#142,Sensor#143,Sensor#144,Sensor#145,Sensor#146,Sensor#147,Sensor#148,Sensor#149,Sensor#150,Sensor#151,Sensor#152,Sensor#153,Sensor#154,Sensor#155,Sensor#156,Sensor#157,Sensor#158,Sensor#159,Sensor#160,Sensor#161,Sensor#162,Sensor#163,Sensor#164,Sensor#165,Sensor#166,Sensor#167,Sensor#168,Sensor#169,Sensor#170,Sensor#171,Sensor#172,Sensor#173,Sensor#174,Sensor#175,Sensor#176,Sensor#177,Sensor#178,Sensor#179,Sensor#180,Sensor#181,Sensor#182,Sensor#183,Sensor#184,Sensor#185,Sensor#186,Sensor#187,Sensor#188,Sensor#189,Sensor#190,Sensor#191,Sensor#192,Sensor#193,Sensor#194,Sensor#195,Sensor#196,Sensor#197,Sensor#198,Sensor#199,Sensor#200,Sensor#201,Sensor#202,Sensor#203,Sensor#204,Sensor#205,Sensor#206,Sensor#207,Sensor#208,Sensor#209,Sensor#210,Sensor#211,Sensor#212,Sensor#213,Sensor#214,Sensor#215,Sensor#216,Sensor#217,Sensor#218,Sensor#219,Sensor#220,Sensor#221,Sensor#222,Sensor#223,Sensor#224,Sensor#225,Sensor#226,Sensor#227,Sensor#228,Sensor#229,Sensor#230,Sensor#231,Sensor#232,Sensor#233,Sensor#234,Sensor#235,Sensor#236,Sensor#237,Sensor#238,Sensor#239,Sensor#240,Sensor#241,Sensor#242,Sensor#243,Sensor#244,Sensor#245,Sensor#246,Sensor#247,Sensor#248,Sensor#249,Sensor#250,Sensor#251,Sensor#252,Sensor#253,Sensor#254,Sensor#255,Sensor#256,Sensor#257,Sensor#258,Sensor#259,Sensor#260,Sensor#261,Sensor#262,Sensor#263,Sensor#264,Sensor#265,Sensor#266,Sensor#267,Sensor#268,Sensor#269,Sensor#270,Sensor#271,Sensor#272,Sensor#273,Sensor#274,Sensor#275,Sensor#276,Sensor#277,Sensor#278,Sensor#279,Sensor#280,Sensor#281,Sensor#282,Sensor#283,Sensor#284,Sensor#285,Sensor#286,Sensor#287,Sensor#288,Sensor#289,Sensor#290,Sensor#291,Sensor#292,Sensor#293,Sensor#294,Sensor#295,Sensor#296,Sensor#297,Sensor#298,Sensor#299,Sensor#300,Sensor#301,Sensor#302,Sensor#303,Sensor#304,Sensor#305,Sensor#306,Sensor#307,Sensor#308,Sensor#309,Sensor#310,Sensor#311,Sensor#312,Sensor#313,Sensor#314,Sensor#315,Sensor#316,Sensor#317,Sensor#318,Sensor#319,Sensor#320,Sensor#321,Sensor#322,Sensor#323,Sensor#324,Sensor#325,Sensor#326,Sensor#327,Sensor#328,Sensor#329,Sensor#330,Sensor#331,Sensor#332,Sensor#333,Sensor#334,Sensor#335,Sensor#336,Sensor#337,Sensor#338,Sensor#339,Sensor#340,Sensor#341,Sensor#342,Sensor#343,Sensor#344,Sensor#345,Sensor#346,Sensor#347,Sensor#348,Sensor#349,Sensor#350,Sensor#351,Sensor#352,Sensor#353,Sensor#354,Sensor#355,Sensor#356,Sensor#357,Sensor#358,Sensor#359,Sensor#360,Sensor#361,Sensor#362,Sensor#363,Sensor#364,Sensor#365,Sensor#366,Sensor#367,Sensor#368,Sensor#369,Sensor#370,Sensor#371,Sensor#372,Sensor#373,Sensor#374,Sensor#375,Sensor#376,Sensor#377,Sensor#378,Sensor#379,Sensor#380,Sensor#381,Sensor#382,Sensor#383,Sensor#384,Sensor#385,Sensor#386,Sensor#387,Sensor#388,Sensor#389,Sensor#390,Sensor#391,Sensor#392,Sensor#393,Sensor#394,Sensor#395,Sensor#396,Sensor#397,Sensor#398,Sensor#399,Sensor#400,Sensor#401,Sensor#402,Sensor#403,Sensor#404,Sensor#405,Sensor#406,Sensor#407,Sensor#408,Sensor#409,Sensor#410,Sensor#411,Sensor#412,Sensor#413,Sensor#414,Sensor#415,Sensor#416,Sensor#417,Sensor#418,Sensor#419,Sensor#420,Sensor#421,Sensor#422,Sensor#423,Sensor#424,Sensor#425,Sensor#426,Sensor#427,Sensor#428,Sensor#429,Sensor#430,Sensor#431,Sensor#432,Sensor#433,Sensor#434,Sensor#435,Sensor#436,Sensor#437,Sensor#438,Sensor#439,Sensor#440,Sensor#441,Sensor#442,Sensor#443,Sensor#444,Sensor#445,Sensor#446,Sensor#447,Sensor#448,Sensor#449,Sensor#450,Sensor#451,Sensor#452,Sensor#453,Sensor#454,Sensor#455,Sensor#456,Sensor#457,Sensor#458,Sensor#459,Sensor#460,Sensor#461,Sensor#462,Sensor#463,Sensor#464,Sensor#465,Sensor#466,Sensor#467,Sensor#468,Sensor#469,Sensor#470,Sensor#471,Sensor#472,Sensor#473,Sensor#474,Sensor#475,Sensor#476,Sensor#477,Sensor#478,Sensor#479,Sensor#480,Sensor#481,Sensor#482,Sensor#483,Sensor#484,Sensor#485,Sensor#486,Sensor#487,Sensor#488,Sensor#489,Sensor#490,Sensor#491,Sensor#492,Sensor#493,Sensor#494,Sensor#495,Sensor#496,Sensor#497,Sensor#498,Sensor#499,Sensor#500,Sensor#501,Sensor#502,Sensor#503,Sensor#504,Sensor#505,Sensor#506,Sensor#507,Sensor#508,Sensor#509,Sensor#510,Sensor#511,Sensor#512,Sensor#513,Sensor#514,Sensor#515,Sensor#516,Sensor#517,Sensor#518,Sensor#519,Sensor#520,Sensor#521,Sensor#522,Sensor#523,Sensor#524,Sensor#525,Sensor#526,Sensor#527,Sensor#528,Sensor#529,Sensor#530,Sensor#531,Sensor#532,Sensor#533,Sensor#534,Sensor#535,Sensor#536,Sensor#537,Sensor#538,Sensor#539,Sensor#540,Sensor#541,Sensor#542,Sensor#543,Sensor#544,Sensor#545,Sensor#546,Sensor#547,Sensor#548,Sensor#549,Sensor#550,Sensor#551,Sensor#552,Sensor#553,Sensor#554,Sensor#555,Sensor#556,Sensor#557,Sensor#558,Sensor#559,Sensor#560,Sensor#561,Sensor#562,Sensor#563,Sensor#564,Sensor#565,Sensor#566,Sensor#567,Sensor#568,Sensor#569,Sensor#570,Sensor#571,Sensor#572,Sensor#573,Sensor#574,Sensor#575,Sensor#576,Sensor#577,Sensor#578,Sensor#579,Sensor#580,Sensor#581,Sensor#582,Sensor#583,Sensor#584,Sensor#585,Sensor#586,Sensor#587,Sensor#588,Sensor#589,Sensor#590";
		//String outputColumns="Yield_Pass_Fail";
		String workingDir = System.getProperty("user.dir");
		System.out.println(workingDir);
		System.out.println(uuid);
		/*
		 * COPY pyAlgorithms.py, secom_test.csv and secom_train.csv to the working directory printed in the console for above println
		 * On server (ec2) it will be in apache's user.dir.
		 * Check your path accordingly and put them there. 
		 * ***** SOMEHOW IT CHECKS FOR SESSION FIRST. IT WON'T WORK UNLESS A USER IS LOGGED IN. NEED TO ASK PURVAL. *****
		 */		
		
		//String[] flowSteps1 = {"Start","Feature Selection","Parameter Setting","Extra Trees Classifier","Decision Tree","End!"};
		String[] flowSteps= flow.split(",");
		
		for(String stepX : flowSteps){
			if(stepX.equalsIgnoreCase("Start")){
				setConsoleLog(uuid,"status","The experiment has been started successfully.");
			}
			if(stepX.equalsIgnoreCase("End!") ){ // END BLOCK
				setConsoleLog(uuid,"status","The experiment completed successfully.");
			}
			if(stepX.equalsIgnoreCase("Boosted Decision Tree") ){ // BOOSTED DECISION TREE
				setConsoleLog(uuid,"status","Starting Boosted Decision Trees regressor on the dataset");
				System.out.println("INSIDE BOOSTED DECISION TREE");
				String message=MLhelper.pyAlgorithms(uuid,"BOOSTED_DECISION_TREE",original_data_path,SPLIT_TYPE, inputColumns, outputColumns,TRAIN_SPLIT_RATIO, OUTPUT_TYPE);
				String messageOutput = message.substring(1, message.length()-2);
				System.out.println(messageOutput);
			}
			if(stepX.equalsIgnoreCase("Decision Tree")){ // DECISION TREE
				setConsoleLog(uuid,"status","Starting Decision Trees regressor on the dataset");
				String message=MLhelper.pyAlgorithms(uuid,"DECISION_TREE",original_data_path,SPLIT_TYPE, inputColumns, outputColumns,TRAIN_SPLIT_RATIO, OUTPUT_TYPE);
				String messageOutput = message.substring(1, message.length()-2);
				System.out.println(messageOutput);
			}
			if(stepX.equalsIgnoreCase("Gradient Boosting")){ // GRADIENT BOOSTING
				setConsoleLog(uuid,"status","Starting Gradient Boosted regressor on the dataset");
				String message=MLhelper.pyAlgorithms(uuid,"GRADIENT_BOOSTING",original_data_path,SPLIT_TYPE, inputColumns, outputColumns,TRAIN_SPLIT_RATIO, OUTPUT_TYPE);
				String messageOutput = message.substring(1, message.length()-2);
				System.out.println(messageOutput);
			}
			if(stepX.equalsIgnoreCase("Logistic Regression")){
				
				setConsoleLog(uuid,"status","The experiment completed successfully. End of experiment.");
			}
			if(stepX.equalsIgnoreCase("Extra Trees Classifier")){
				
				setConsoleLog(uuid,"status","Starting Extra Trees Classifier for feature reduction...");
				String reducedFeatures = MLhelper.pyFeatures(uuid,"EXTRA_TREE_CLASSIFIER",workingDir+"/secom_full.csv", inputColumns, outputColumns,Integer.parseInt(NUMBER_OF_FEATURES));
				inputColumns=reducedFeatures;
			}
			if(stepX.equalsIgnoreCase("K-best Features")){
				
				setConsoleLog(uuid,"status","The experiment completed successfully. End of experiment.");
			}
			if(stepX.equalsIgnoreCase("Recursive Feature Elimination")){
				
				setConsoleLog(uuid,"status","The experiment completed successfully. End of experiment.");
			}
			if(stepX.equalsIgnoreCase("Parameter Setting")){
				
				setConsoleLog(uuid,"status","Setting parameters as defined in Parameter Setting box.");
			}
			if(stepX.equalsIgnoreCase("Feature Selection")){
				setConsoleLog(uuid,"status","Features have been selected as defined in Feature Selection box");
			}
			
			
		}
		
		
		
		
	}

	
	
}
