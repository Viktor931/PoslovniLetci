package business.flyers.Services;

import business.flyers.Constants.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PaginationServiceTest {
    @InjectMocks
    private PaginationService paginationService;

    @Test
    public void testFrom(){
        String pageNumber = "4";
        Assert.assertEquals(paginationService.from(pageNumber), (Integer.parseInt(pageNumber) - 1) * Constants.Pagination.ENTRIES_PER_PAGE);
    }

    @Test
    public void testTo(){
        String pageNumber = "4";
        Assert.assertEquals(paginationService.to(pageNumber), Integer.parseInt(pageNumber)  * Constants.Pagination.ENTRIES_PER_PAGE);
    }

    @Test
    public void testMaxPages(){
        int numberOfEntries = 19;
        Assert.assertEquals(paginationService.maxPages(numberOfEntries), (numberOfEntries + Constants.Pagination.ENTRIES_PER_PAGE) / Constants.Pagination.ENTRIES_PER_PAGE);
    }
}
