package whiteboard.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import whiteboard.Whiteboard;
import whiteboard.WhiteboardRepo;

abstract class WhiteboardRepoContractTest {
    final Whiteboard ny = new Whiteboard("NY", null);
    final Whiteboard sf = new Whiteboard("SF", null);
    protected WhiteboardRepo repo;

    @BeforeEach
    void before() {
        createRepo();
        repo.save(ny);
        repo.save(sf);
    }

    protected abstract void createRepo();

    @Test
    void findsByName() {
        assertThat(ny, is(repo.findByName(ny.getName())));
        assertThat(sf, is(repo.findByName(sf.getName())));
    }

    @Test
    void createsUniqueId() {
        assertThat(ny.getId(), notNullValue());
        assertThat(sf.getId(), notNullValue());
        assertThat(ny.getId(), not(equalTo(sf.getId())));
    }
}

