import java.net.*;
import java.util.*;
import org.json.*;
import java.io.*;


public class TocHw4 {
	public static void main(String[] args) throws JSONException{
		ArrayList<String> time_list = new ArrayList<String>(0);
		ArrayList<String> road_list = new ArrayList<String>(0);
		ArrayList<Integer> highprice = new ArrayList<Integer>(0);
		ArrayList<Integer> lowprice = new ArrayList<Integer>(0);
		String address = "", time = "", max_road = "";
		int trade = 1,index = 0, highestprice = 0,address_length = 0;
		int price = 0, highest = 0, lowest = 0, finalhigh = 0, finallow = 0;
		
		try 
		{
			URL url = new URL(args[0]);
			URLConnection connect = url.openConnection();
			InputStreamReader stream = new InputStreamReader(connect.getInputStream(),"UTF-8");
			JSONArray json_data = new JSONArray(new JSONTokener(stream));

			for(int json_index = 0; json_index < json_data.length(); json_index++)
			{
				address = json_data.getJSONObject(json_index).getString("土地區段位置或建物區門牌");
				
				if(address.lastIndexOf("路") != -1){
					address_length = address.lastIndexOf("路");
				}
				else if(address.lastIndexOf("街") != -1){
					address_length = address.lastIndexOf("街");
				}
				else if(address.lastIndexOf("大道") != -1){
					address_length = address.lastIndexOf("大道") + 1;
				}
				else if(address.lastIndexOf("巷") != -1){
					address_length = address.lastIndexOf("巷");
				}
				else{
					continue;
				}

				address = address.substring(0, address_length + 1);
				time = Integer.toString(json_data.getJSONObject(json_index).getInt("交易年月"));
				price = json_data.getJSONObject(json_index).getInt("總價元");
				
				for(index = 0; index < road_list.size(); index++){
					if(address.equals(road_list.get(index))){
						break;
					}
				}
				if(index == road_list.size()){
					road_list.add(address);
					time_list.add(time);
					highprice.add(price);
					lowprice.add(price);
				}
				else{
					for(; index < road_list.size(); index++){
						if(!address.equals(road_list.get(index))){
							break;
						}
						
						if(time.equals(time_list.get(index))){
							if(price > highprice.get(index)){
								highprice.set(index, price);
							}
							else if(price < lowprice.get(index)){
								lowprice.set(index, price);
							}
							break;
						}
					}
					if(index < road_list.size() && !address.equals(road_list.get(index))){
						road_list.add(index, address);
						time_list.add(index, time);
						highprice.add(index, price);
						lowprice.add(index, price);
					}
					else if(index == road_list.size()){
						road_list.add(index, address);
						time_list.add(index, time);
						highprice.add(index, price);
						lowprice.add(index, price);
					}
				}
			}
			
			trade = 1;
			for(index = 1; index < road_list.size(); index++){
				if(road_list.get(index).equals(road_list.get(index - 1))){
					trade++;
				}
				else{
					if(trade > highestprice){
						highestprice = trade;
						max_road = road_list.get(index - 1);
					}
					trade = 1;
				}
			}
			if(trade > highestprice){
				highestprice = trade;
				max_road = road_list.get(index - 1);
			}
			
			for(index = 0; !road_list.get(index).equals(max_road); index++);
			trade = 1;
			highest = highprice.get(index);
			lowest = lowprice.get(index);
			for(index = index + 1; index < road_list.size(); index++){
				if(road_list.get(index).equals(road_list.get(index - 1))){
					trade++;
					if(highprice.get(index) > highest){
						highest = highprice.get(index);
					}
					if(lowprice.get(index) < lowest){
						lowest = lowprice.get(index);
					}
				}
				else{
					if(trade == highestprice){
						max_road = road_list.get(index - 1);
						finalhigh = highest;
						finallow = lowest;
						System.out.println(max_road + " " + finalhigh + " " + finallow);
					}
					trade = 1;
					
					highest = highprice.get(index);
					lowest = lowprice.get(index);
				}
			}
			if(trade == highestprice){
				max_road = road_list.get(index - 1);
				
				finalhigh = highest;
				finallow = lowest;
				
				System.out.println(max_road + " " + finalhigh + " " + finallow);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
