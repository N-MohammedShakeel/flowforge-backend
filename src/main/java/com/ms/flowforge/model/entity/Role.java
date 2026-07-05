package com.ms.flowforge.model.entity;

/**
 * Application roles. Kept as a simple enum for MVP.
 * Can be promoted to a full @Entity with fine-grained permissions later.
 */
public enum Role {
    USER,
    ADMIN
}
