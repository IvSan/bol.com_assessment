package dev.ivsan.bolassessment.controller;

import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.GetBoardResponseDTO;
import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveRequestDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveResponseDTO;
import dev.ivsan.bolassessment.service.BoardManagerImpl;
import dev.ivsan.bolassessment.service.ValidationService;
import io.github.resilience4j.core.functions.Either;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
            Either<Pair<HttpStatus, String>, ListBoardsRequestDTO> errorOrRequest =
                    validationService.validateGetBoardsRequest(request);
            if (errorOrRequest.isLeft()) {
                ListBoardsResponseDTO response = new ListBoardsResponseDTO(errorOrRequest.getLeft().getRight());
                LOG.warn("Get boards request invalid: {}", response);
                return new ResponseEntity<>(response, errorOrRequest.getLeft().getLeft());
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

    @GetMapping("/boards/{id}")
    @Operation(summary = "List all boards you were involved.")
    public ResponseEntity<GetBoardResponseDTO> getBoard(
            @RequestBody GetBoardRequestDTO request,
            @PathVariable("id") UUID boardId
    ) {
        LOG.info("New '/boards/{}' GET request with the request body: {}", boardId, request);
        try {
            Either<Pair<HttpStatus, String>, GetBoardRequestDTO> errorOrRequest =
                    validationService.validateGetBoardRequest(request, boardId);
            if (errorOrRequest.isLeft()) {
                GetBoardResponseDTO response = new GetBoardResponseDTO(errorOrRequest.getLeft().getRight());
                LOG.warn("Get board request invalid: {}", response);
                return new ResponseEntity<>(response, errorOrRequest.getLeft().getLeft());
            }
            GetBoardResponseDTO response = boardManager.getBoard(errorOrRequest.get(), boardId);
            LOG.info("Get board request completed successfully: {}", response);
            return new ResponseEntity<>(response, OK);
        } catch (Exception ex) {
            LOG.error("Get board request unsuccessful, internal error", ex);
            return new ResponseEntity<>(
                    new GetBoardResponseDTO("Internal server error, please try again later"),
                    INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/boards/{id}/moves")
    @Operation(summary = "Submit your move for the board.")
    public ResponseEntity<SubmitMoveResponseDTO> submitMove(
            @RequestBody SubmitMoveRequestDTO request,
            @PathVariable("id") UUID boardId
    ) {
        LOG.info("New '/boards/{}/moves' POST request with the request body: {}", boardId, request);
        try {
            Either<Pair<HttpStatus, String>, SubmitMoveRequestDTO> errorOrRequest =
                    validationService.validateSubmitMoveRequest(request, boardId);
            if (errorOrRequest.isLeft()) {
                SubmitMoveResponseDTO response = new SubmitMoveResponseDTO(errorOrRequest.getLeft().getRight());
                LOG.warn("Submit move request invalid: {}", response);
                return new ResponseEntity<>(response, errorOrRequest.getLeft().getLeft());
            }
            SubmitMoveResponseDTO response = boardManager.submitMove(errorOrRequest.get(), boardId);
            LOG.info("Submit move request completed successfully: {}", response);
            return new ResponseEntity<>(response, OK);
        } catch (Exception ex) {
            LOG.error("Submit move request unsuccessful, internal error", ex);
            return new ResponseEntity<>(
                    new SubmitMoveResponseDTO("Internal server error, please try again later"),
                    INTERNAL_SERVER_ERROR
            );
        }
    }
}
