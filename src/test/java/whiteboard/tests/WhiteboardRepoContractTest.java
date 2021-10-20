package whiteboard.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import whiteboard.Whiteboard;
import whiteboard.WhiteboardRepo;

public abstract class WhiteboardRepoContractTest {
	protected WhiteboardRepo repo;

	Whiteboard ny = new Whiteboard("NY", null);
	Whiteboard sf = new Whiteboard("SF", null);

	@BeforeEach
	public void before() {
		createRepo();
		repo.save(ny);
		repo.save(sf);
	}

	protected abstract void createRepo();

	@Test
	public void findsByName() {
		assertEquals(ny, repo.findByName(ny.getName()));
		assertEquals(sf, repo.findByName(sf.getName()));
	}

	@Test
	public void createsUniqueId() {
		assertTrue(ny.getId() != null);
		assertTrue(sf.getId() != null);
		assertNotEquals(ny.getId(), sf.getId());
	}
}

