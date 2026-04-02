package com.aura.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MigrationRequest {
    private String vendor;      // e.g., "tableau", "pbi"
    private String auth;        // e.g., "google", "sso", "dummy"
    private String db;          // e.g., "postgres", "mysql"
    private String os;          // e.g., "win", "mac"
    private String biValidator; // e.g., "tableau", "pbi"
    private String uploader;    // e.g., "file", "folder"
}