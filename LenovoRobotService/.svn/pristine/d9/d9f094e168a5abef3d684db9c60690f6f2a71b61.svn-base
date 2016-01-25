//package com.lenovo.main.util;
//
//import java.util.ArrayList;
//
//import com.lenovo.main.MIService.BaseService;
//
//public class RecognizeResult {
//	// 用来存放中国人的ID集合
//	private static ArrayList<Integer> zh_peopleId = new ArrayList<Integer>();
//	// 用来存放中国人的姓名的集合
//	private static ArrayList<String> zh_peopleNames = new ArrayList<String>();
//	// 用来存放外国人的集合
//	private static ArrayList<Integer> us_peopleId = new ArrayList<Integer>();
//	// 用来存放外国人的姓名的集合
//	private static ArrayList<String> us_peopleNames = new ArrayList<String>();
//
//	/**
//	 * 把识别结果传递进来进行解析
//	 * 
//	 * @param result
//	 */
//	public static void setRecognizeResult(String result) {
//		if (result != null) {
//			int length = result.length();
//			if (length == 1) {
//				// 说明只有一个人的结果
//				int parseInt = Integer.parseInt(result);
//				if (parseInt == 2 || parseInt == 3 || parseInt == 11
//						|| parseInt == 12 || parseInt == 13 || parseInt == 20) {
//					us_PeopleMothed(parseInt);
//				} else {
//					zh_PeopleMothed(parseInt);
//				}
//			} else {
//				us_peopleId.clear();
//				zh_peopleId.clear();
//				
//				String[] split = result.split(",");
//				for (String str : split) {
//					int parseInt = Integer.parseInt(str);
//
//					if (parseInt == 2 || parseInt == 3 || parseInt == 11
//							|| parseInt == 12 || parseInt == 13
//							|| parseInt == 20) {
//						us_peopleId.add(parseInt);
//					} else {
//						zh_peopleId.add(parseInt);
//					}
//				}
//				usAndZh(us_peopleId,zh_peopleId);
//			}
//		}
//	}
//
//	private static void usAndZh(ArrayList<Integer> us_peopleId2,
//			ArrayList<Integer> zh_peopleId2) {
//		if(us_peopleId2.size() > 0){
//			
//		}
//	}
//
//	private static int zh_count = 0;
//	private static String zh_names = "";
//
//	private static void zh_PeopleMothed(int parseInt) {
//		zh_peopleId.clear();
//		zh_peopleNames.clear();
//		zh_peopleId.add(parseInt);
//		for (Integer index : zh_peopleId) {
//			String name = BaseService.peopleMap.get(index);
//			if (name != null) {
//				zh_peopleNames.add(name);
//			} else {
//				zh_count++;
//			}
//		}
//		for (String name : zh_peopleNames) {
//			zh_names += name;
//		}
//		if (zh_count == 0) {
//			BaseService.compound.speaking(zh_names, true, false);
//		} else {
//			BaseService.compound.speaking("尊贵的客人您好初次见面请多关照", true, false);
//		}
//		
//		zh_names = "";
//		zh_count = 0;
//	}
//
//	private static int us_count = 0;
//	private static String us_names = "";
//
//	private static void us_PeopleMothed(int parseInt) {
//		us_peopleId.clear();
//		us_peopleNames.clear();
//
//		us_peopleId.add(parseInt);
//		for (Integer index : us_peopleId) {
//			String name = BaseService.peopleMap.get(index);
//			if (name != null) {
//				us_peopleNames.add(name);
//			} else {
//				us_count++;
//			}
//		}
//		for (String name : us_peopleNames) {
//			us_names += name;
//		}
//
//		if (us_count == 0) {
//			BaseService.compound.speaking(us_names, false, false);
//		} else {
//			BaseService.compound.speaking(
//					"Dear guest, please take care of your first meeting.",
//					false, false);
//		}
//	}
// }
