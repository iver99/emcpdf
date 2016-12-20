package oracle.sysman.emaas.platform.emcpdf.cache.api;

import oracle.sysman.emaas.platform.emcpdf.cache.tool.Element;

/**
 * Created by chehao on 2016/12/9.
 */
public interface ICacheUnit {
    public boolean put(String key,Element e);

    public Object get(String key);

    public boolean remove(String key);

    public void clearCache();
}
