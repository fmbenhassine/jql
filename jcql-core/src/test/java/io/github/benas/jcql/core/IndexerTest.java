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

        assertThat(database.count("class")).isEqualTo(1);
        assertThat((Integer) database.count("interface")).isEqualTo(1);
        assertThat((Integer) database.count("annotation")).isEqualTo(1);
        assertThat((Integer) database.count("enumeration")).isEqualTo(1);
        assertThat((Integer) database.count("field")).isEqualTo(1);
        assertThat((Integer) database.count("constructor")).isEqualTo(1);
        assertThat((Integer) database.count("method")).isEqualTo(4);
        assertThat((Integer) database.count("parameter")).isEqualTo(2);
        assertThat((Integer) database.count("compilation_unit")).isEqualTo(5);
    }
}
