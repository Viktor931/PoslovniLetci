package business.flyers.Services;

import business.flyers.Constants.Constants;
import org.springframework.stereotype.Service;

@Service
public class PaginationService {

    public int from(String pageNumber) {
        return (Integer.parseInt(pageNumber) - 1) * Constants.Pagination.ENTRIES_PER_PAGE;
    }

    public int to(String pageNumber) {
        return Integer.parseInt(pageNumber) * Constants.Pagination.ENTRIES_PER_PAGE;
    }

    public int maxPages(int numberOfEntries) {
        return (numberOfEntries + Constants.Pagination.ENTRIES_PER_PAGE) / Constants.Pagination.ENTRIES_PER_PAGE;
    }
}
