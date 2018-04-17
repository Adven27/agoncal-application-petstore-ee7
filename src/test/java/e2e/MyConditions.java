package e2e;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.WebElementsCollection;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import java.util.List;

public class MyConditions {

    public static CollectionCondition dataEqualTo(String... data) {
        return dataEqualTo(DataSets.data(data));
    }

    public static CollectionCondition dataEqualTo(final Table<Integer, String, String> dataSet) {
        return new CollectionCondition() {
            private final Table<Integer, String, String> expected = dataSet;
            private Table<Integer, String, String> actual;

            @Override
            public boolean apply(@Nullable List<WebElement> elements) {
                actual = HashBasedTable.create();
                Integer i = 1;
                for (WebElement row : elements) {
                    actual.put(i, "cat", row.findElement(By.className("it-cat")).getText());
                    actual.put(i, "sum", row.findElement(By.className("it-sum")).getText());
                    i++;
                }
                return actual.equals(expected);
            }

            @Override
            public void fail(WebElementsCollection collection, List<WebElement> elements, Exception lastError, long timeoutMs) {
                throw new TableMismatch(collection, actual, expected, timeoutMs);
            }

            @Override
            public String toString() {
                return "Exact table " + expected;
            }
        };
    }

    public static class TableMismatch extends UIAssertionError {
        public TableMismatch(WebElementsCollection collection, Table actual, Table expected, long timeoutMs) {
            super("\nActual: " + actual + "\nExpected: " + expected + "\nCollection: " + collection.description());
            super.timeoutMs = timeoutMs;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + ' ' + getMessage() + uiDetails();
        }
    }
}
