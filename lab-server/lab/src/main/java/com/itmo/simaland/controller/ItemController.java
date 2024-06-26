package com.itmo.simaland.controller;

import com.itmo.simaland.dto.filter.ItemFilter;
import com.itmo.simaland.dto.paging.ListResponse;
import com.itmo.simaland.dto.item.ItemRequest;
import com.itmo.simaland.dto.item.ItemResponse;
import com.itmo.simaland.dto.item.UpdateItemRequest;
import com.itmo.simaland.dto.mapper.ItemMapper;
import com.itmo.simaland.dto.paging.PaginationRequest;
import com.itmo.simaland.model.entity.Item;
import com.itmo.simaland.service.ItemService;
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


@Tag(name = "Item Controller", description = "Item Controller")
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    @Operation(summary = "Get all items")
    @ApiResponse(responseCode = "200", description = "Items list", content =  @Content)
    public ListResponse<ItemResponse> getAllItems(
            PaginationRequest paginationRequest,
            @Valid ItemFilter itemFilter) {

        PageRequest pageRequest = paginationRequest.toPageRequest();
        Page<Item> page = itemService.getAllItems(pageRequest, itemFilter);

        return itemMapper.pageToItemListResponse(page) ;
    }

    @PostMapping
    @Operation(summary = "Create new item")
    @PreAuthorize("hasAuthority('EDIT_ITEMS')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid item data provided", content = @Content)
    })
    public ItemResponse createItem(@RequestBody @Valid ItemRequest itemRequest) {
        Item item = itemMapper.toItem(itemRequest);
        Item savedItem = itemService.createItem(item);
        return itemMapper.toItemResponse(savedItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an item")
    @PreAuthorize("hasAuthority('EDIT_ITEMS')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid item data provided", content = @Content)
    })
    public ItemResponse updateItem(@PathVariable Long id, @RequestBody @Valid UpdateItemRequest updateRequest) {
        Item item = itemService.updateItem(id, updateRequest);
        return itemMapper.toItemResponse(item);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item")
    @PreAuthorize("hasAuthority('EDIT_ITEMS')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content)
    })
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
