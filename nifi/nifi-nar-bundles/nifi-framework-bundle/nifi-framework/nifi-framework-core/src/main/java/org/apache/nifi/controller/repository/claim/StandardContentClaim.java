/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.controller.repository.claim;


/**
 * <p>
 * A ContentClaim is a reference to a given flow file's content. Multiple flow files may reference the same content by both having the same content claim.</p>
 *
 * <p>
 * Must be thread safe</p>
 *
 */
public final class StandardContentClaim implements ContentClaim, Comparable<ContentClaim> {

    private final ResourceClaim resourceClaim;
    private final int hashCode;
    private volatile long offset;
    private volatile long length;

    public StandardContentClaim(final ResourceClaim resourceClaim, final long offset) {
        this.resourceClaim = resourceClaim;
        this.offset = offset;
        this.length = -1L;
        this.hashCode = calculateHashCode();
    }

    public void setLength(final long length) {
        this.length = length;
    }

    public void setOffset(final long offset) {
        this.offset = offset;
    }

    private int calculateHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hashCode;
        result = prime * result + (int) (length ^ length >>> 32);
        result = prime * result + (int) (offset ^ offset >>> 32);
        result = prime * result + (resourceClaim == null ? 0 : resourceClaim.hashCode());
        return result;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ContentClaim)) {
            return false;
        }

        final ContentClaim other = (ContentClaim) obj;
        if (length != other.getLength()) {
            return false;
        }

        if (offset != other.getOffset()) {
            return false;
        }

        return resourceClaim.equals(other.getResourceClaim());
    }

    @Override
    public int compareTo(final ContentClaim o) {
        final int resourceComp = resourceClaim.compareTo(o.getResourceClaim());
        if (resourceComp != 0) {
            return resourceComp;
        }

        return Long.compare(offset, o.getOffset());
    }

    @Override
    public ResourceClaim getResourceClaim() {
        return resourceClaim;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public long getLength() {
        return length;
    }
}
