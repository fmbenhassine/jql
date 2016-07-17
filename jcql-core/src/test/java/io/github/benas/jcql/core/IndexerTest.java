package io.github.benas.jcql.core;

import io.github.benas.jcql.Database;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexerTest {

    private File sourceCodeDirectory, databaseDirectory;

    private Indexer indexer;

    @Before
    public void setUp() throws Exception {
        indexer = new Indexer();
        sourceCodeDirectory = new File("src/test/java/io/github/benas/jcql/code").getAbsoluteFile();
        databaseDirectory = new File("target").getAbsoluteFile();
    }

    @Test
    public void testCodeIndexing() throws Exception {
        indexer.index(sourceCodeDirectory, databaseDirectory);

        Database database = new Database(databaseDirectory);

        assertThat(database.count("class")).isEqualTo(2);
        assertThat(database.count("interface")).isEqualTo(2);
        assertThat(database.count("annotation")).isEqualTo(1);
        assertThat(database.count("enumeration")).isEqualTo(1);
        assertThat(database.count("field")).isEqualTo(2);
        assertThat(database.count("constructor")).isEqualTo(1);
        assertThat(database.count("method")).isEqualTo(6);
        assertThat(database.count("parameter")).isEqualTo(2);
        assertThat(database.count("implements")).isEqualTo(2);
        assertThat(database.count("extends")).isEqualTo(1);
        assertThat(database.count("compilation_unit")).isEqualTo(7);
    }
}
