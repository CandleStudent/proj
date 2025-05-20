package ru.delivery.dto;

public record GetProfileDto(
    String name,
    String surname,
    String phone,
    String email
) {}
