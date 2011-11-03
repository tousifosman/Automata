package generateMinimalNFA;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import automata.CharToken;
import automata.NFA;

public class MinimalDFAGenerator {
	
	private Map<String, CharToken> charClasses;
	private Map<String, NFA> identifiers;
	
	
	
	public MinimalDFAGenerator(String fileName){
		
		
        try {
        	FileReader file = new FileReader(fileName);
        	BufferedReader reader = new BufferedReader(file);
        	String line;
        	
        	while((line = reader.readLine()) != null){
      
        		if(line.startsWith("%%")&& line.contains("character")){
        			charClasses = new HashMap<String, CharToken>();
        			while((line=reader.readLine())!=null && !line.startsWith("%%")){
        				System.out.println(line);
        				
        				char[] chars = line.toCharArray();
        				StringBuilder CNBuilder = new StringBuilder();
        				int i;
        				for(i=0; i<chars.length; i++){
        					if(chars[i]!=' '&& chars[i]!='\t'){
        						CNBuilder.append(chars[i]);
        					}
        					else{
        						break;
        					}
        				}
        				String charClassName = CNBuilder.toString();
        				
        				while(chars[i] !='['){
        					i++;
        				}
        				boolean opposite =false;
        				
        				Set<Character> charSet = new HashSet<Character>();
        				for(i=i+1; i<chars.length; i++){
        					
        					if(chars[i]==']'){
        						
        						break;
        					}
        					
        					if(chars[i] == '^'){
        						opposite = true;				
        					}
        				
        					if(Constants.upperCaseList.contains(chars[i])){
        						char startChar  = chars[i];
        						charSet.add(startChar);
        						if(chars[i+1]=='-'){
        							i++;
        							int startIndex = Constants.upperCaseList.indexOf(startChar);
        							i++;
        							int endIndex = Constants.upperCaseList.indexOf(chars[i])+1;
        							for(int index = startIndex; index<endIndex; index++){
        								charSet.add(Constants.upperCaseList.get(index));
        							}
        							
        						}        						
        					}
        					else if(Constants.lowerCaseList.contains(chars[i])){
        						char startChar  = chars[i];
        						charSet.add(startChar);
        						if(chars[i+1]=='-'){
        							i++;
        							int startIndex = Constants.lowerCaseList.indexOf(startChar);
        							i++;
        							int endIndex = Constants.lowerCaseList.indexOf(chars[i])+1;
        							for(int index = startIndex; index<endIndex; index++){
        								charSet.add(Constants.lowerCaseList.get(index));
        							}
        						} 
        					}
        					else if(Constants.digitsList.contains(chars[i])){
        						char startChar  = chars[i];
        						charSet.add(startChar);
        						if(chars[i+1]=='-'){
        							i++;
        							int startIndex = Constants.digitsList.indexOf(startChar);
        							i++;
        							int endIndex = Constants.digitsList.indexOf(chars[i])+1;
        							for(int index = startIndex; index<endIndex; index++){
        								charSet.add(Constants.digitsList.get(index));
        							}
        						} 
        					
        					}
        				
        				}
        				if(opposite){
        					while(chars[i] !='$'){
        						i++;
        					}
        					StringBuilder builder = new StringBuilder();
        					for(i=i; i<chars.length; i++){
        						if(chars[i] == ' ' || chars[i] == '\t'){
        							break;
        						}
        						builder.append(chars[i]);
        					}
        					String className = builder.toString().trim();
        					Set<Character> newCharSet = new HashSet<Character>();
        					for(Character c : charClasses.get(className).chars){
        						newCharSet.add(c);
        					}
        					for(Character c : charSet){
        						newCharSet.remove(c);
        					}
        					charSet = newCharSet;
        				}
        				CharToken token = new CharToken(charClassName, charSet);
        				charClasses.put(charClassName, token);
        				
        			}
            		if(line.startsWith("%%") && line.contains("Token")){
            			identifiers = new HashMap<String, NFA>();
            			while((line = reader.readLine()) != null){
            				System.out.println(line);
            				
            				constuctNFA(line);
            			}			        		
            		}
        			
        		}
        	
        	}
        	
        	
            
        } catch (Exception ex) {
            //System.out.println("Error: Cannot find file");
            //System.out.println(ex.getStackTrace());
        	ex.printStackTrace();
        } 
		
	}

	private void constuctNFA(String regex){
		if(regex.length()<1){
			return;
		}
		//JW TODO construct DFAs here
		
	}
	
	
	private Queue<String> getTokens(String regex){
		
		
		char[] chars = regex.toCharArray();
		int i=0;
		StringBuilder idNameBuilder = new StringBuilder(); 
		for(i=i; i< chars.length; i++){
			if(chars[i]==' ' || chars[i] == '\t'){
				break;
			}
			idNameBuilder.append(chars[i]);
		}
		String idName = idNameBuilder.toString();
		while(chars[i]==' ' || chars[i] == '\t'){
			i++;
		}
		for(i=i; i<chars.length; i++){
			System.out.print(chars[i]);
		}
		System.out.println();
		return null;
	}
	
	
	
	

	public Map<String, CharToken> getCharClasses() {
		return charClasses;
	}



	public Map<String, NFA> getIdentifiers() {
		return identifiers;
	}
	
	
	

}
