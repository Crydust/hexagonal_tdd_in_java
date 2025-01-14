package whiteboard.persistence;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whiteboard.core.Whiteboard;
import whiteboard.core.WhiteboardRepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

abstract class WhiteboardRepoContract {
    final Whiteboard ny = new Whiteboard("NY", null);
    final Whiteboard sf = new Whiteboard("SF", null);
    WhiteboardRepo repo;

    @BeforeEach
    void before() throws Exception {
        repo = createRepo();
        repo.initialize();
        repo.save(ny);
        repo.save(sf);
    }

    @AfterEach
    void tearDown() throws Exception {
        repo.deleteAll();
        repo.dispose();
    }

    protected abstract WhiteboardRepo createRepo();

    @Test
    void findsById() {
        assertThat(repo.findById(ny.getId()), is(ny));
        assertThat(repo.findById(sf.getId()), is(sf));
    }

    @Test
    void findsByName() {
        assertThat(repo.findByName(ny.getName()), is(ny));
        assertThat(repo.findByName(sf.getName()), is(sf));
    }

    @Test
    void findsAll() {
        assertThat(repo.findAll(), contains(ny, sf));
    }

    @Test
    void createsUniqueId() {
        assertThat(ny.getId(), notNullValue());
        assertThat(sf.getId(), notNullValue());
        assertThat(ny.getId(), not(equalTo(sf.getId())));
    }
}

