package org.yzr.vo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import org.yzr.model.Package;
import org.yzr.utils.date.DateUtil;
import org.yzr.utils.file.PathManager;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.time.ZoneOffset;
import java.util.Arrays;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class PackageViewModel {
    private String downloadURL;
    private String safeDownloadURL;
    private String iconURL;
    private String installURL;
    private String previewURL;
    private String id;
    private String version;
    private String bundleID;
    private String name;
    private long createTime;
    private String buildVersion;
    private String displaySize;
    private String displayTime;
    private boolean iOS;
    private String type;
    private List<String> devices;
    private int deviceCount;
    private String message;
    private String iconKey;

    public PackageViewModel(Package aPackage, HttpServletRequest request) {
        String httpURL = PathManager.request(request).getBaseURL();
        String httpsURL = PathManager.request(request).useHttps().getBaseURL();
        this.downloadURL = httpURL + "/p/" + aPackage.getId();
        this.safeDownloadURL = httpsURL + "/p/" + aPackage.getId();
        this.id = aPackage.getId();
        this.version = aPackage.getVersion();
        this.bundleID = aPackage.getBundleID();
        this.name = aPackage.getName();
        this.createTime = aPackage.getCreateTime();
        this.buildVersion = aPackage.getBuildVersion();
        this.iconKey = aPackage.getIconFile().getKey();
        this.displaySize = String.format("%.2f MB", aPackage.getSize() / (1.0F * FileUtils.ONE_MB));
        Date updateTime = new Date(this.createTime);
        String displayTime = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(updateTime);
        this.displayTime = displayTime;
        if (aPackage.getPlatform().equals("ios")) {
            this.iOS = true;
            String url = httpsURL + "/m/" + aPackage.getId();
            try {
                this.installURL = "itms-services://?action=download-manifest&url=" + url;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (aPackage.getPlatform().equals("android")) {
            this.iOS = false;
            this.installURL = httpURL + "/p/" + aPackage.getId();
        }
        this.previewURL = httpURL + "/s/" + aPackage.getApp().getShortCode() + "?id=" + aPackage.getId();
        if (this.isiOS()) {
            if (aPackage.getProvision() == null) {
                this.type = "内测版";
            } else {
                if (aPackage.getProvision().isEnterprise()) {
                    this.type = "企业版";
                } else {
                    if ("AdHoc".equalsIgnoreCase(aPackage.getProvision().getType())) {
                        this.type = "内测版";
                    } else {
                        this.type = "商店版";
                    }
                    this.deviceCount = aPackage.getProvision().getDeviceCount();
                    if (aPackage.getProvision().getDeviceCount() > 0) {
                        this.devices = Arrays.asList(aPackage.getProvision().getDevices());
                    }
                }
            }
        } else {
            this.type = "内测版";
        }
        String message = "";
        if (StringUtils.hasLength(aPackage.getExtra())) {
            Map<String, String> extra = (Map<String, String>) JSON.parse(aPackage.getExtra());
            if (extra.containsKey("jobName")) {
                message += " 任务名:" + extra.get("jobName");
            }

            if (extra.containsKey("buildNumber")) {
                message += " 编号:#" + extra.get("buildNumber");
            }
        }
        this.message = message;
        try {
            this.iconURL = httpsURL + "/fetch/" + aPackage.getIconFile().getKey();
        } catch (Exception e){}
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public String getSafeDownloadURL() {
        return safeDownloadURL;
    }

    public String getIconURL() {
        return iconURL;
    }

    public String getInstallURL() {
        return installURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getBundleID() {
        return bundleID;
    }

    public String getName() {
        return name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public String getDisplaySize() {
        return displaySize;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public boolean isiOS() {
        return iOS;
    }

    public String getType() {
        return type;
    }

    public List<String> getDevices() {
        return devices;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public String getMessage() {
        return message;
    }

    public String getIconKey() {
        return iconKey;
    }
}
