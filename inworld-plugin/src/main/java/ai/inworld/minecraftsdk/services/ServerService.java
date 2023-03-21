package ai.inworld.minecraftsdk.services;

import ai.inworld.minecraftsdk.utils.NetUtils;

import java.util.UUID;

public final class ServerService {
    
    public static final String SERVER_ID = UUID.randomUUID().toString();
    public static final String SERVER_IP = NetUtils.getIp();

}
