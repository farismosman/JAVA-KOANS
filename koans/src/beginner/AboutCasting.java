package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;
import static com.sandwich.util.Assert.fail;

import com.sandwich.koan.Koan;

@SuppressWarnings("unused")
public class AboutCasting {
    
	@Koan
	public void longPlusInt() {
		int a = 6;
		long b = 10;
		Object c = a + b;
		assertEquals(c, (long) 16);
		assertEquals(c instanceof Integer, false);
		assertEquals(c instanceof Long, true);
	}
    
    @Koan
	public void forceIntTypecast() {
		long a = 2147483648L;
		// What happens if we force a long value into an int?
		int b = (int)a;
		assertEquals(b, Integer.MIN_VALUE);
	}
   
    @Koan
	public void implicitTypecast() {
    	int a = 1;
    	int b = Integer.MAX_VALUE;
		long c = a + b; // still overflows int... which is the Integer.MIN_VALUE, the operation occurs prior to assignment to long
		assertEquals(c, (long) Integer.MIN_VALUE);
	}
    
    interface Sleepable {
    	String sleep();
    }
    class Child implements Sleepable{
    	public String sleep() { 
    		return "zzzz"; 
    	}
    }
    class Parent extends Child { 
    	public String complain() { 
    		return "TPS reports don't even have a cover letter!"; 
    	} 
    }
    class GrandParent extends Parent {
    	public String complain() { 
    		return "Get your feet off the davenport!"; 
    	} 
    }
    
    @Koan
    public void downCastWithInerhitance() {
    	GrandParent grandParent = new GrandParent();
    	Parent parentReference = grandParent; // Why isn't there an explicit cast?
    	assertEquals(grandParent instanceof GrandParent, true);
    	assertEquals(parentReference instanceof GrandParent, true);
    	assertEquals(parentReference instanceof Parent, true);
    	assertEquals(parentReference instanceof Child, true);
    }
    
    @Koan
    public void downCastAndPolymophism() {
    	GrandParent grandParent = new GrandParent();
    	Parent parentReference = grandParent;
    	// If the result is unexpected, consider the difference between an instance and its reference
    	assertEquals(parentReference.complain(), "Get your feet off the davenport!");
    }
    
    @Koan
    public void upCastWithInheritance() {
    	Child grandParent = new GrandParent();
    	Parent parentReference = (Parent)grandParent; // Why do we need an explicit cast here?
    	GrandParent grandParentReference = (GrandParent)parentReference; // Or here?
    	assertEquals(grandParentReference instanceof GrandParent, true);
    	assertEquals(grandParentReference instanceof Parent, true);
    	assertEquals(grandParentReference instanceof Child, true);
    }
    
    @Koan
    public void upCastAndPolymophism() {
    	Child grandParent = new GrandParent();
    	Parent parent = (GrandParent)grandParent;
    	// Think about the result. Did you expect that? Why?
    	// How is that different from above?
    	assertEquals(parent.complain(), "Get your feet off the davenport!");
    }
    
    @Koan
    public void classCasting(){
    	try{
    		Object o = new Object(); 	// we're downcasting way too far here - would it be possible
    									// to even compile this koan had we done what was safe, and 
    									// held the reference as Sleepable?
    		((Sleepable)o).sleep();
    	}catch(ClassCastException x){
           	//fail("Object does not implement Sleepable, maybe one of the people classes do?");
    	}
    }
    
    @Koan
    public void complicatedCast() {
    	Child parent = new Parent();
    	// How can we access the parent's ability to "complain" - if the reference is held as a superclass? 
    	assertEquals(new Parent().complain(), ((Parent) parent).complain());
    }
   
}
