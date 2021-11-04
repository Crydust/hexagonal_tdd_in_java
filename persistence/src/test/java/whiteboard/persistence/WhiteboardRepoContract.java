package whiteboard.persistence;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whiteboard.Whiteboard;
import whiteboard.WhiteboardRepo;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

abstract class WhiteboardRepoContract {
    final Whiteboard ny = new Whiteboard("NY", null);
    final Whiteboard sf = new Whiteboard("SF", null);
    protected WhiteboardRepo repo;

    @BeforeEach
    void before() {
        createRepo();
        repo.save(ny);
        repo.save(sf);
    }

    @AfterEach
    void tearDown() {
        repo.deleteAll();
    }

    protected abstract void createRepo();

    @Test
    void findsById() {
        MatcherAssert.assertThat(ny, is(repo.findById(ny.getId())));
        MatcherAssert.assertThat(sf, is(repo.findById(sf.getId())));
    }

    @Test
    void findsByName() {
        MatcherAssert.assertThat(ny, is(repo.findByName(ny.getName())));
        MatcherAssert.assertThat(sf, is(repo.findByName(sf.getName())));
    }

    @Test
    void createsUniqueId() {
        MatcherAssert.assertThat(ny.getId(), notNullValue());
        MatcherAssert.assertThat(sf.getId(), notNullValue());
        MatcherAssert.assertThat(ny.getId(), not(equalTo(sf.getId())));
    }
}

