import { ChevronLeftIcon, ChevronRightIcon } from '../Common/Icons.jsx';
import './Pagination.css';

function Pagination({ currentPage, totalPages, onPageChange }) {
  if (totalPages <= 1) {
    return null;
  }

  const pages = [];

  for (let pageNumber = 1; pageNumber <= totalPages; pageNumber += 1) {
    if (
      pageNumber === 1 ||
      pageNumber === totalPages ||
      Math.abs(pageNumber - currentPage) <= 1
    ) {
      pages.push(pageNumber);
    }
  }

  const visiblePages = [];

  pages.forEach((pageNumber, index) => {
    const previousPage = pages[index - 1];

    if (previousPage && pageNumber - previousPage > 1) {
      visiblePages.push('ellipsis-' + pageNumber);
    }

    visiblePages.push(pageNumber);
  });

  return (
    <nav className="pagination" aria-label="Phân trang">
      <button
        className="pagination__button pagination__button--control"
        type="button"
        onClick={() => onPageChange(Math.max(1, currentPage - 1))}
        disabled={currentPage === 1}
      >
        <ChevronLeftIcon />
      </button>

      {visiblePages.map((pageNumber) =>
        typeof pageNumber === 'number' ? (
          <button
            key={pageNumber}
            className={`pagination__button ${pageNumber === currentPage ? 'is-active' : ''}`}
            type="button"
            onClick={() => onPageChange(pageNumber)}
          >
            {pageNumber}
          </button>
        ) : (
          <span key={pageNumber} className="pagination__ellipsis">
            ...
          </span>
        ),
      )}

      <button
        className="pagination__button pagination__button--control"
        type="button"
        onClick={() => onPageChange(Math.min(totalPages, currentPage + 1))}
        disabled={currentPage === totalPages}
      >
        <ChevronRightIcon />
      </button>
    </nav>
  );
}

export default Pagination;