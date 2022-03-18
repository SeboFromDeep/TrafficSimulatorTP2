package simulator.factories;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import simulator.model.DequeingStrategy;
import simulator.model.MoveAllStrategy;

class MoveAllStrategyBuilderTest {

	@Test
	void test_1() {
		MoveAllStrategyBuilder eb = new MoveAllStrategyBuilder();
		
		String inputJSon = " { \"type\" : \"move_all_dqs\",  \"data\" : {} }";
		DequeingStrategy o = eb.createInstance(new JSONObject(inputJSon));
		assertTrue( o instanceof MoveAllStrategy );
		
	}

	@Test
	void test_2() {
		MoveAllStrategyBuilder eb = new MoveAllStrategyBuilder();
		
		String inputJSon = " { \"type\" : \"move_all_dqs\" }";
		DequeingStrategy o = eb.createInstance(new JSONObject(inputJSon));
		assertTrue( o instanceof MoveAllStrategy );
		
	}

}
