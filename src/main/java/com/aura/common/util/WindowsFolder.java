package com.aura.common.util;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("win")
public class WindowsFolder implements FolderManager{
    @Override
    public void createFolders() {
    }
}
