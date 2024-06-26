package com.itmo.simaland.controller;

import com.itmo.simaland.dto.paging.ListResponse;
import com.itmo.simaland.dto.pickUpPoint.PickUpPointRequest;
import com.itmo.simaland.dto.pickUpPoint.PickUpPointResponse;
import com.itmo.simaland.dto.mapper.PickUpPointMapper;
import com.itmo.simaland.dto.paging.PaginationRequest;
import com.itmo.simaland.dto.pickUpPoint.UpdatePickUpPointRequest;
import com.itmo.simaland.model.entity.PickUpPoint;
import com.itmo.simaland.service.PickUpPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "PickUpPoint Controller", description = "PickUpPoint Controller")
@RequestMapping("/pick-up-points")
@RequiredArgsConstructor
public class PickUpPointController {

    private final PickUpPointService pickUpPointService;
    private final PickUpPointMapper pickUpPointMapper;


    @GetMapping
    @Operation(summary = "Get all pick-up points")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ListResponse<PickUpPointResponse> getPickUpPoints(PaginationRequest paginationRequest) {
        PageRequest pageRequest = paginationRequest.toPageRequest();
        Page<PickUpPoint> page = pickUpPointService.getAllPickUpPoints(pageRequest);

        return pickUpPointMapper.pageToPickUpPointListResponse(page) ;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PICKUP_POINT')")
    @Operation(summary = "Create a new pick-up point")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid pick-up point data provided", content = @Content)
    })
    public PickUpPointResponse createPickUpPoint(@Valid @RequestBody PickUpPointRequest request) {
        PickUpPoint pickUpPoint = pickUpPointService.createPickUpPoint(request);
        return pickUpPointMapper.toResponse(pickUpPoint);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_PICKUP_POINT')")
    @Operation(summary = "Update a pick-up point")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pick-up point updated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pick-up point not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid pick-up point data provided", content = @Content)
    })
    public PickUpPointResponse updatePickUpPoint(@PathVariable Long id, @RequestBody @Valid UpdatePickUpPointRequest updateRequest) {
        PickUpPoint updatedPickUpPoint = pickUpPointService.updatePickUpPoint(id, updateRequest);
        return pickUpPointMapper.toResponse(updatedPickUpPoint);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EDIT_PICKUP_POINT')")
    @Operation(summary = "Delete a pick-up point")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pick-up point deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pick-up point not found", content = @Content)
    })
    public void deletePickUpPoint(@PathVariable Long id) {
        pickUpPointService.deletePickUpPoint(id);
    }
}
