package com.topsy.jmxproxy.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Host implements JsonSerializable {
    private static final Logger LOG = LoggerFactory.getLogger(Host.class);

    private Map<String, MBean> mbeans;
    private Map<String, Domain> domains;

    public Host() {
        mbeans = new HashMap<String, MBean>();
        domains = new HashMap<String, Domain>();
    }

    public MBean addMBean(String domain, String mbeanName) {
        if (!domains.containsKey(domain)) {
            domains.put(domain, new Domain());
        }

        MBean mbean = domains.get(domain).addMBean(mbeanName);
        mbeans.put(mbeanName, mbean);

        return mbean;
    }

    public Set<String> getDomains() {
        return domains.keySet();
    }

    public Domain getDomain(String domain) {
        return domains.get(domain);
    }

    public MBean getMBean(String mbean) {
        return mbeans.get(mbean);
    }

    public void serialize(JsonGenerator jgen, SerializerProvider sp) throws IOException, JsonProcessingException {
        buildJson(jgen);
    }

    public void serializeWithType(JsonGenerator jgen, SerializerProvider sp, TypeSerializer ts) throws IOException, JsonProcessingException {
        buildJson(jgen);
    }

    public void buildJson(JsonGenerator jgen) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        for (Map.Entry<String, MBean>mbeanEntry : mbeans.entrySet()) {
            jgen.writeObjectField(mbeanEntry.getKey(), mbeanEntry.getValue());
        }
        jgen.writeEndObject();
    }
}
