package dev.ivsan.bolassessment.controller;

import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.service.BoardManagerImpl;
import dev.ivsan.bolassessment.service.ValidationService;
import io.github.resilience4j.core.functions.Either;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
public class BoardsController {

    @Autowired
    ValidationService validationService;

    @Autowired
    BoardManagerImpl boardManager;

    private final static Logger LOG = LoggerFactory.getLogger(BoardsController.class);

    @GetMapping("/boards")
    @Operation(summary = "List all boards you were involved.")
    public ResponseEntity<ListBoardsResponseDTO> listBoards(@RequestBody ListBoardsRequestDTO request) {
        LOG.info("New '/boards' POST request with the request body: {}", request);
        try {
            Either<String, ListBoardsRequestDTO> errorOrRequest = validationService.validateGetBoardsRequest(request);
            if (errorOrRequest.isLeft()) {
                ListBoardsResponseDTO response = new ListBoardsResponseDTO(errorOrRequest.getLeft());
                LOG.warn("Get boards request invalid: {}", response);
                return new ResponseEntity<>(response, BAD_REQUEST);
            }
            ListBoardsResponseDTO response = boardManager.listBoards(errorOrRequest.get());
            LOG.info("List boards request completed successfully: {}", response);
            return new ResponseEntity<>(response, OK);
        } catch (Exception ex) {
            LOG.error("List boards request unsuccessful, internal error", ex);
            return new ResponseEntity<>(
                    new ListBoardsResponseDTO("Internal server error, please try again later"),
                    INTERNAL_SERVER_ERROR
            );
        }
    }
}
