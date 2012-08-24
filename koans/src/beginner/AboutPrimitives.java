package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class AboutPrimitives {
	
	@Koan()
	public void byteSize() {
		assertEquals(Byte.SIZE, 8);
	}
	
	@Koan()
	public void shortSize() {
		assertEquals(Short.SIZE, 16);
	}
	
	@Koan
	public void integerSize() {
		assertEquals(Integer.SIZE, 32);
	}
	
	@Koan
	public void longSize() {
		assertEquals(Long.SIZE, 64);
	}
	
	@Koan
	public void charSizeAndValue() {
		// a char basically is an unsigned int
		assertEquals(Character.SIZE,16);
		assertEquals(Character.MIN_VALUE,'\u0000');
		assertEquals(Character.MAX_VALUE,'\uFFFF');
	}
	
	// Floating Points
	@Koan
	public void floatSize() {
		assertEquals(Float.SIZE,32);
	}
	
	@Koan
	public void doubleSize() {
		assertEquals(Double.SIZE, 64);
	}
}
