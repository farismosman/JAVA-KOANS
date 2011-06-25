package com.sandwich.koan.path;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.koan.Koan;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.path.xmltransformation.KoanElementAttributes;
import com.sandwich.koan.path.xmltransformation.XmlToPathTransformer;
import com.sandwich.koan.path.xmltransformation.XmlToPathTransformerImpl;
import com.sandwich.util.Counter;
import com.sandwich.util.io.FileUtils;

public abstract class PathToEnlightenment {

	static Path theWay;
	static String suiteName;
	static String koanMethod;
	static XmlToPathTransformer xmlToPathTransformer;

	private PathToEnlightenment(){} // non instantiable
	
	static Path createPath(){
		try{
			return getXmlToPathTransformer().transform();
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
	private static XmlToPathTransformer getXmlToPathTransformer(){
		if(xmlToPathTransformer == null){
			try {
				xmlToPathTransformer = new XmlToPathTransformerImpl(KoanConstants.PATH_XML_LOCATION, suiteName, koanMethod);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return xmlToPathTransformer;
	}
	
	public static void filterBySuite(String suite){
		suiteName = suite;
	}
	
	public static void filterByKoan(String koan){
		koanMethod = koan;
	}
	
	public static Path getPathToEnlightment(){
		if(theWay == null){
			theWay = createPath();
		}
		return theWay;
	}
	
	public static class FileFormatException extends RuntimeException{
		private static final long serialVersionUID = -1343169944770684376L;
		public FileFormatException(String message){
			super(message);
		}
	}
	
	public static class Path implements Iterable<Entry<String, Map<String, Map<String, KoanElementAttributes>>>>{
		private Map<String, Map<String, Map<String, KoanElementAttributes>>> koanMethodsBySuiteByPackage;
		public Path(){}
		public int getTotalNumberOfKoans() {
			Counter total = new Counter();
			Iterator<Entry<String, Map<String, Map<String, KoanElementAttributes>>>> koanMethodsBySuiteByPackageIter = 
				getKoanMethodsBySuiteByPackage();
			while(koanMethodsBySuiteByPackageIter.hasNext()){
				Entry<String, Map<String, Map<String, KoanElementAttributes>>> e = koanMethodsBySuiteByPackageIter.next();
				for(Entry<String, Map<String, KoanElementAttributes>> e1 : e.getValue().entrySet()){
					countKoanAnnotationsInJavaFileGivenClassName(e1.getKey(), total);
				}
			}
			return (int)total.getCount();
		}
		
		private void countKoanAnnotationsInJavaFileGivenClassName(String className, Counter total) {
			String[] lines = FileUtils.getContentsOfOriginalJavaFile(className).split(KoanConstants.EOLS);
			String koanClassSimpleNameWithAnnotationPrefix = '@'+Koan.class.getSimpleName();
			for(String line : lines){
				String trimmedLine = line.trim();
				if(trimmedLine.contains(koanClassSimpleNameWithAnnotationPrefix)
						&& !trimmedLine.startsWith("//") && !trimmedLine.startsWith("*") && !trimmedLine.startsWith("/*")){
					total.count();
				}
			}
		}
		
		public Iterator<Entry<String, Map<String, Map<String, KoanElementAttributes>>>> iterator() {
			return getKoanMethodsBySuiteByPackage();
		}
		
		public Path stubKoanMethodsBySuiteByClass(Map<String, Map<String, Map<String, KoanElementAttributes>>> koanMethodsBySuiteByPackage){
			// unlike other collections in this app, this actually needs to remain mutable after the reference is
			// stored and utilized internally. this is so the DynamicClassLoader can swap out references to 
			// any dynamic classes
			this.koanMethodsBySuiteByPackage = koanMethodsBySuiteByPackage;
			return this;
		}
		protected Iterator<Entry<String, Map<String, Map<String, KoanElementAttributes>>>> getKoanMethodsBySuiteByPackage() {
			return koanMethodsBySuiteByPackage.entrySet().iterator();
		}
		@Override public boolean equals(Object o){
			if(o == this){
				return true;
			}
			if(o instanceof Path){
				if(getKoanMethodsBySuiteByPackage() == ((Path)o).getKoanMethodsBySuiteByPackage()){
					return true;
				}
				if(getKoanMethodsBySuiteByPackage() == null || ((Path)o).getKoanMethodsBySuiteByPackage() == null
						|| getKoanMethodsBySuiteByPackage().getClass() != ((Path)o).getKoanMethodsBySuiteByPackage().getClass()){
					return false;
				}
				Iterator<Entry<String,Map<String, Map<String, KoanElementAttributes>>>> i1 = 
					getKoanMethodsBySuiteByPackage();
				Iterator<Entry<String, Map<String, Map<String, KoanElementAttributes>>>> i2 = 
					((Path)o).getKoanMethodsBySuiteByPackage();
				while(i1.hasNext()){
					Map<String, Map<String, KoanElementAttributes>> m1 = i1.next().getValue();
					Map<String, Map<String, KoanElementAttributes>> m2 = i2.next().getValue();
					if(m1 == m2){
						continue;
					}
					if(			m1 == null 
							||  m2 == null
							||  m1.size() != m2.size()
							||  m1.getClass() != m2.getClass()){
						return false;
					}
					Iterator<Entry<String, Map<String, KoanElementAttributes>>> ii1 = m1.entrySet().iterator();
					Iterator<Entry<String, Map<String, KoanElementAttributes>>> ii2 = m2.entrySet().iterator();
					while(ii1.hasNext()){
						Entry<String, Map<String, KoanElementAttributes>> e1 = ii1.next();
						Entry<String, Map<String, KoanElementAttributes>> e2 = ii2.next();
						if(!e1.getKey().getClass().equals(e2.getKey().getClass())){
							return false;
						}
						if(!e1.getValue().equals(e2.getValue())){
							return false;
						}
					}
				}
			}
			return true;
		}
		@Override public int hashCode(){
			return getKoanMethodsBySuiteByPackage().hashCode();
		}
		@Override public String toString(){
			return getKoanMethodsBySuiteByPackage().toString();
		}
	}
}
