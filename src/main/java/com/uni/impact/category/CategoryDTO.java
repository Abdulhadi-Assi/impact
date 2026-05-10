package com.uni.impact.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class CategoryDTO {

    private Long categoryId;

    @NotNull
    @Size(max = 100)
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
